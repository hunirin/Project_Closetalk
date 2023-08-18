package team.closetalk.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.user.dto.CustomUserDetails;
import team.closetalk.user.dto.JwtTokenDto;
import team.closetalk.user.service.UserService;
import team.closetalk.user.utils.JwtUtils;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
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

    @PostMapping("/login")
    public JwtTokenDto loginUser(@RequestParam("loginId") String loginId
                            , @RequestParam("password") String password
    ){
        //반환된 값은 아이디 유무, 소셜여부까지 확인된 것(우선 없는 것만 통과)
        CustomUserDetails responseUser = userService.loadUserByUsername(loginId);
        if (!passwordEncoder.matches(password, responseUser.getPassword())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        JwtTokenDto response = new JwtTokenDto();
        response.setToken(jwtUtils.generateToken(responseUser));

        return response;

    }

    //회원가입 이미지 등록
    @PostMapping(value = "registerImage"
                , consumes = "multipart/form-data")
    public String registerProfileImage(@RequestParam("profileImage") MultipartFile profileImage){
        return "upload profile Image success";
    }


}
