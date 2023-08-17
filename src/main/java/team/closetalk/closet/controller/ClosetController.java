package team.closetalk.closet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.closetalk.closet.dto.ClosetDto;
import team.closetalk.closet.dto.ClosetItemDto;
import team.closetalk.closet.service.ClosetService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/closet")
public class ClosetController {
    private final ClosetService closetService;

    @PostMapping
    public List<ClosetDto> readAllCloset() {
        return closetService.readCloset();
    }

    @PostMapping("/{closetId}")
    public List<ClosetItemDto> readItems(@PathVariable("closetId") Long closetId,
                                         @RequestParam(name = "category", required = false) String category) {
        if (category == null) return closetService.readByCloset(closetId);
        else return closetService.readByCategory(closetId, category);
    }
}
