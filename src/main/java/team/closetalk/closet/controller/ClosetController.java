package team.closetalk.closet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import team.closetalk.closet.service.ClosetService;

@RestController
@RequiredArgsConstructor
public class ClosetController {
    private final ClosetService closetService;
    @PostMapping("/closet")
    public void readCloset() {
        closetService.readByCloset();
    }

    public void readCategory() {
        closetService.readByCategory();
    }
}
