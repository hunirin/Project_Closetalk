package team.closetalk.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.closetalk.user.dto.CustomUserDetails;
import team.closetalk.user.service.UserService;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ViewController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("loginPage")
    public String viewLoginForm(){
        return "user/login-form";
    }

    @GetMapping("joinPage")
    public String viewJoinForm() {
        return "user/join-form";
    }

    @GetMapping("test")
    public String testForm() {
        return "user/test";
    }

    // 로그아웃
    @PostMapping("logout")
    public String logout(Authentication authentication) {
        CustomUserDetails responseUser = userService.loadUserByUsername(authentication.getName());
        userService.logout(responseUser);
        return "ootd/ootdMain";
    }
}
