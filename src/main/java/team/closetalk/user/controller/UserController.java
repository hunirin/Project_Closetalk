package team.closetalk.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.user.dto.*;
import team.closetalk.user.service.EmailSendService;
import team.closetalk.user.service.UserService;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSendService emailSendService;

    //C
    //이메일 인증 처리
    @ResponseBody
    @PostMapping(value = "/sendEmail")
    public ResponseEntity<EmailAuthDto> sendAuthEmail(@RequestBody String email){
        String code = emailSendService.makeEmailAuth(email.substring(1,email.length()-1));

        EmailAuthDto emailAuthDto = new EmailAuthDto();
        emailAuthDto.setAuthCode(code);

        return ResponseEntity.status(HttpStatus.OK).body(emailAuthDto);
    }

    //회원가입
    @PostMapping(value = "/register"
            , consumes = "multipart/form-data"
    )
    public void registerUser(  //ResponseEntity<> 로 리턴타입 변경할 것 .
            @RequestParam("loginId") String loginId
            , @RequestParam("password") String password
            , @RequestParam("password-check") String passwordCheck
            , @RequestParam("nickname") String nickname
            , @RequestParam("email") String email
            , @RequestParam("social") String social
            , @RequestParam("profile-image") MultipartFile profileImage
    ){
        if(password.equals(passwordCheck)){
            userService.createUser(CustomUserDetails.builder()
                    .loginId(loginId)
                    .password(passwordEncoder.encode(password))
                    .nickname(nickname)
                    .email(email)
                    .social(social)
                    .profileImage(profileImage)
                    .build()
            );

//            return ResponseEntity.status(HttpStatus.OK).body("join success");
        } else {
            log.warn("password mismatch");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("password mismatch");
        }

    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginReqDto) {
        CustomUserDetails responseUser = userService.loadUserByUsername(loginReqDto.getLoginId());
        if (!passwordEncoder.matches(loginReqDto.getPassword(), responseUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok()
                .body(userService.login(responseUser));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        CustomUserDetails responseUser = userService.loadUserByUsername(authentication.getName());
        userService.logout(responseUser);
        return ResponseEntity.ok().build();
    }


    //회원정보 가져오기
    @GetMapping
    public CustomUserDetails readUserOne(Authentication authentication){
        return userService.loadUserByUsername(CustomUserDetails.fromAuthentication(authentication).getLoginId());
    }


    //회원정보 수정
    @PutMapping
    public String updateUserOne(Authentication authentication, @RequestBody CustomUserDetails customUserDetails){
        customUserDetails.setLoginId(CustomUserDetails.fromAuthentication(authentication).getLoginId());
        customUserDetails.setPassword(passwordEncoder.encode(customUserDetails.getPassword()));
        userService.updateUser(customUserDetails);

        return "Update user information successful";
    }

    //프로필 이미지 변경
    @PutMapping("/profile-image")
    public String updateProfileImage(Authentication authentication, @RequestParam("profile-image") MultipartFile profileImage) throws IOException {
        userService.updateProfileImage(authentication, profileImage);
        return "Update profile image successful";
    }


    //회원 탈퇴
    @DeleteMapping
    public String deleteUserOne(Authentication authentication){
        userService.deleteUser(CustomUserDetails.fromAuthentication(authentication).getLoginId());
        //로그아웃처리
        return "Delete user successful";
    }

    @PostMapping("/test")
    public ResponseEntity<?> test(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails responseUser = userService.loadUserByUsername(authentication.getName());
            return ResponseEntity.ok(responseUser.getNickname());
        } else {
            // 인증되지 않은 경우, 예외 처리 또는 다른 응답을 반환할 수 있습니다.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
