package team.closetalk.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.user.dto.CustomUserDetails;
import team.closetalk.user.entity.UserEntity;
import team.closetalk.user.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsManager {
    private final UserRepository userRepository;

    private final String PROFILE_IMAGE_DIRECTORY = "src/main/resources/static/images/profile/";
    private final String PROFILE_IMAGE_DEFAULT = "default_profile.png";

    private String saveProfileImage(MultipartFile profileImage, String loginId){
        //이미지 저장할 디렉토리 없으면 생성
        try {
            Files.createDirectories(Path.of(PROFILE_IMAGE_DIRECTORY ));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String imagePath = PROFILE_IMAGE_DIRECTORY + PROFILE_IMAGE_DEFAULT;

        //이미지를 등록한 경우 imagePath 변경 및 이미지 파일 저장
        if(profileImage != null && !profileImage.isEmpty()){
            //이미지 확장자명 가져오기
            String[] originalFilenameSplit = profileImage.getOriginalFilename().split("\\.");
            String extension = originalFilenameSplit[originalFilenameSplit.length - 1];

            //이미지 저장
            imagePath = PROFILE_IMAGE_DIRECTORY + loginId + "." + extension;
            try {
                profileImage.transferTo(Path.of(imagePath));
            } catch(IOException e){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return imagePath;
    }

    private boolean isEmpty(String value){
        if(value == null || value.equals("")) return true;
        return false;
    }
    private boolean isEmail(String email){
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);

        if(m.matches()) return true;
        else return false;
    }

    //회원가입
    @Override
    public void createUser(UserDetails user) {
        CustomUserDetails customUserDetails = (CustomUserDetails) user;

        //validation check
        String loginId = customUserDetails.getLoginId();
        String nickname = customUserDetails.getNickname();
        String email = customUserDetails.getEmail();

        //값 여부 확인 - loginId, password, nickname, email
        if(isEmpty(loginId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if(isEmpty(customUserDetails.getPassword())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if(isEmpty(nickname)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if(isEmpty(email)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        //양식확인 - email
        if(!isEmail(customUserDetails.getEmail())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        //중복확인 - loginId, nickname, email(이미 소셜로그인으로 가입한 경우 안내)
        if(userRepository.existsByLoginId(loginId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if(userRepository.existsByNickname(nickname)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if(userRepository.existsByEmail(email)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        //create user
        try{
            UserEntity userEntity = customUserDetails.newEntity();
            userEntity.setCreatedAt(Date.from(Instant.now()));
            if(userEntity.getSocial().equals("")) userEntity.setSocial(null);
            //profileImage 저장 및 경로 반환
            userEntity.setProfileImageUrl(saveProfileImage(customUserDetails.getProfileImage(), loginId));
            userRepository.save(userEntity);

        } catch (ClassCastException e){
            log.error("fail to cast to {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //로그인

    @Override
    public CustomUserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByLoginId(loginId);

        if(optionalUser.isEmpty()) throw new UsernameNotFoundException(loginId);

        return CustomUserDetails.fromEntity(optionalUser.get());
    }


    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByLoginId(username);
    }

}
