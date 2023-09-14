package team.closetalk.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
@RequestMapping("/")
public class ViewController {

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
}
