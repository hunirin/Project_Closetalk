package team.closetalk.closet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/closet")
@RequiredArgsConstructor
public class ClosetViewController {

    @GetMapping("/{nickname}")
    public String closetList(@PathVariable("nickname") String nickname) {
        return "closet/my-closet";
    }
}