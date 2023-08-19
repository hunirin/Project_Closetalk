package team.closetalk.closet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.closetalk.closet.dto.ClosetItemDto;
import team.closetalk.closet.service.ClosetItemService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/closet/item")
public class ClosetItemController {
    private final ClosetItemService closetItemService;

    @PostMapping("/{itemId}")
    public ClosetItemDto closetItemRead(@PathVariable("itemId") Long itemId) {
        return closetItemService.readClosetItem(itemId);
    }
}
