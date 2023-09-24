package team.closetalk.mypage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("views")
public class DMViewController {
    @GetMapping("/dm")
    public String dm() {
        return "dm/direct-message";
    }
}
