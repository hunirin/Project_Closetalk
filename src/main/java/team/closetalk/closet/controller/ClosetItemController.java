package team.closetalk.closet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import team.closetalk.closet.dto.ClosetItemDto;
import team.closetalk.closet.service.ClosetItemService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/closet/item")
public class ClosetItemController {
    private final ClosetItemService closetItemService;

    @PostMapping("/{itemId}")
    public ClosetItemDto closetItemRead(@PathVariable("itemId") Long itemId,
                                        Authentication authentication) {
        return closetItemService.readClosetItem(itemId, authentication);
    }

    @PutMapping("/{itemId}")
    public void closetItemModify(@PathVariable("itemId") Long itemId,
                                 @RequestParam Map<String, String> itemParams,
                                 Authentication authentication) {
        closetItemService.modifyClosetItem(itemId, itemParams, authentication);
    }

    @DeleteMapping("/{itemId}")
    public void closetItemDelete(@PathVariable("itemId") Long itemId,
                                 Authentication authentication) {
        closetItemService.deleteClosetItem(itemId, authentication);
    }
}
