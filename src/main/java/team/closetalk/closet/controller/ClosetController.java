package team.closetalk.closet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import team.closetalk.closet.dto.ClosetItemDto;
import team.closetalk.closet.service.ClosetService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClosetController {
    private final ClosetService closetService;
    @PostMapping("/closet/{closetId}")
    public List<ClosetItemDto> readCloset(@PathVariable("closetId") Long closetId) {
        return closetService.readByCloset(closetId);
    }

    public void readCategory() {
        closetService.readByCategory();
    }
}
