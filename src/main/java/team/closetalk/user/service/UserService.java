package team.closetalk.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.user.dto.CustomUserDetails;
import team.closetalk.user.dto.LoginResponseDto;
import team.closetalk.user.entity.UserEntity;
import team.closetalk.user.repository.UserRepository;
import team.closetalk.user.utils.JwtUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
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
    private final JwtUtils jwtUtils;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    private final String PROFILE_IMAGE_DIRECTORY_THYMELEAF = "/static/images/profile/";
    private final String PROFILE_IMAGE_DIRECTORY = "src/main/resources/static/images/profile/";
    private final String PROFILE_IMAGE_DEFAULT = "default_profile.png";

    private String saveProfileImage(MultipartFile profileImage, String loginId){
        //이미지 저장할 디렉토리 없으면 생성
        try {
            Files.createDirectories(Path.of(PROFILE_IMAGE_DIRECTORY ));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String imagePath = PROFILE_IMAGE_DIRECTORY_THYMELEAF + PROFILE_IMAGE_DEFAULT;

        //이미지를 등록한 경우 imagePath 변경 및 이미지 파일 저장
        if(profileImage != null && !profileImage.isEmpty()){
            //이미지 확장자명 가져오기
            String[] originalFilenameSplit = profileImage.getOriginalFilename().split("\\.");
            String extension = originalFilenameSplit[originalFilenameSplit.length - 1];

            //이미지 저장
            imagePath = PROFILE_IMAGE_DIRECTORY_THYMELEAF + loginId + "." + extension;
            try {
                profileImage.transferTo(Path.of(PROFILE_IMAGE_DIRECTORY + loginId + "." + extension));

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
    public LoginResponseDto login(CustomUserDetails responseUser) {
        log.info("로그인 유저의 닉네임: {}", responseUser.getNickname());

        String accessToken = jwtUtils.generateAccessToken(responseUser);
        String refreshToken = tokenService.getRefreshToken(responseUser.getLoginId());

        if (refreshToken == null || refreshToken.isEmpty()) {
            refreshToken = jwtUtils.generateRefreshToken(responseUser);
            tokenService.saveRefreshToken(refreshToken, responseUser.getLoginId());
        }

        return new LoginResponseDto(responseUser.getNickname(), accessToken);
    }

    //로그아웃
    public void logout(CustomUserDetails responseUser) {
        tokenService.deleteRefreshToken(responseUser.getLoginId());
    }

    @Override
    public CustomUserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByLoginId(loginId);

        if(optionalUser.isEmpty()) throw new UsernameNotFoundException(loginId);
        if(optionalUser.get().getIsDeleted()) throw new UsernameNotFoundException(loginId);

        return CustomUserDetails.fromEntity(optionalUser.get());
    }


    @Override
    public void updateUser(UserDetails user) {
        CustomUserDetails customUserDetails = (CustomUserDetails) user;

        Optional<UserEntity> optionalUser = userRepository.findByLoginId(customUserDetails.getLoginId());
        if(optionalUser.isEmpty()) throw new UsernameNotFoundException(customUserDetails.getLoginId());

        UserEntity userEntity = optionalUser.get();
        userEntity.setPassword(customUserDetails.getPassword());
        userEntity.setNickname(customUserDetails.getNickname());

        userRepository.save(userEntity);
    }

    public void updateProfileImage(Authentication authentication, MultipartFile profileImage) throws IOException {
        String loginId = CustomUserDetails.fromAuthentication(authentication).getLoginId();

        Optional<UserEntity> optionalUser = userRepository.findByLoginId(loginId);
        if(optionalUser.isEmpty()) throw new UsernameNotFoundException(loginId);

        UserEntity userEntity = optionalUser.get();
        if(deleteProfileImageFile(userEntity.getProfileImageUrl())){
            String imagePath = saveProfileImage(profileImage, loginId);
            userEntity.setProfileImageUrl(imagePath);

            userRepository.save(userEntity);

        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    private boolean deleteProfileImageFile(String profileImageUrl) throws IOException {
        if(profileImageUrl.contains("default_profile.png")) return true;

        return Files.deleteIfExists(Path.of("src/main/resources" + profileImageUrl));
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByLoginId(username);
        if(optionalUser.isEmpty()) throw new UsernameNotFoundException(username);

        String profileImageUrl = userRepository.findByLoginId(username).get().getProfileImageUrl();
        try{
            deleteProfileImageFile(profileImageUrl);
        } catch (Exception e){
            log.error(e.getMessage());
        }
        optionalUser.get().setIsDeleted(true);
        userRepository.save(optionalUser.get());
    }

    // 아이디 찾기
    public String findUserId(String email) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()) return "입력하신 회원 정보로 가입된 아이디가 존재하지 않습니다.";

        UserEntity user = optionalUser.get();
        String loginId = user.getLoginId();
        return "입력하신 회원 정보로 가입된 아이디는 " + loginId + " 입니다.";
    }

    // 비밀번호 찾기 (계정 확인)
    public String findPassword(String loginId, String email) {
        Optional<UserEntity> optionalUser = userRepository.findByLoginIdAndEmail(loginId, email);

        if(optionalUser.isEmpty()) return "입력하신 회원 정보로 가입된 계정이 존재하지 않습니다.";

        return "입력하신 이메일로 임시 비밀번호를 발급합니다.";
    }

    //비밀번호 찾기 (임시 비밀번호 생성 및 DB 저장)
    private static char getRandomChar(String characters, SecureRandom random) {
        int randomIndex = random.nextInt(characters.length());
        return characters.charAt(randomIndex);
    }

    public String createPassword(String email) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String specialCharacters = "!@#$%^&*()_+-=[]{}|;:,.<>?";

        SecureRandom random = new SecureRandom();
        StringBuilder passwordBuilder = new StringBuilder();

        passwordBuilder.append(getRandomChar(characters, random));
        passwordBuilder.append(getRandomChar(digits, random));
        passwordBuilder.append(getRandomChar(specialCharacters, random));

        String allCharacters = characters + digits + specialCharacters;
        for (int i = passwordBuilder.length(); i < 8; i++) {
            passwordBuilder.append(getRandomChar(allCharacters, random));
        }

        char[] tempPassword = passwordBuilder.toString().toCharArray();
        for (int i = 0; i < tempPassword.length; i++) {
            int randomIndex = random.nextInt(tempPassword.length);
            char temp = tempPassword[i];
            tempPassword[i] = tempPassword[randomIndex];
            tempPassword[randomIndex] = temp;
        }

        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty())
            return "입력하신 회원 정보로 가입된 계정이 존재하지 않습니다.";

        UserEntity user = optionalUser.get();
        user.setTempPassword(passwordEncoder.encode(new String(tempPassword)));
        userRepository.save(user);
        return new String(tempPassword);
    }

    // 임시 비밀번호 발급 메일 작성
    public MimeMessage createPasswordMail(String email) {
        String password = createPassword(email);
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom("hun053@naver.com");
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[Closetalk] 임시 비밀번호 발급");
            String body = "";
            body += "<h2>" + "closetalk 임시 비밀번호" + "</h2>";
            body += "<h3>" + "회원님께서 요청하신 임시 비밀번호가 발급되었습니다." + "</h3>";
            body += "<h3>" + "아래의 임시비밀번호를 사용하여 closetalk에 로그인 후 새로운 비밀번호로 변경하시기 바랍니다." + "</h3>";
            body += "<h1> " + password + "</h1>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    //임시 비밀번호 발급 메일 전송
    public String sendTempPassword(String email) {
        MimeMessage message = createPasswordMail(email);
        javaMailSender.send(message);

        return "임시 비밀번호 발급 메일 전송 완료";
    }


    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByLoginId(username);
    }

    public boolean userExistsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
