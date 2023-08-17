package team.closetalk.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.closetalk.user.dto.CustomUserDetails;
import team.closetalk.user.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @PostMapping(value = "/register"
            , consumes = "multipart/form-data"
    )
    public String registerUser(
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
            return "redirect:/users/loginPage";
        } else {
            log.warn("password mismatch");
            return "mismatch";
        }

    }

    //회원가입 이미지 등록
    @PostMapping(value = "registerImage"
                , consumes = "multipart/form-data")
    public String registerProfileImage(@RequestParam("profileImage") MultipartFile profileImage){
        return "upload profile Image success";
    }


}
