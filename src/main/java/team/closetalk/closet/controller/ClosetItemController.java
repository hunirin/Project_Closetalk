package team.closetalk.closet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import team.closetalk.closet.dto.ClosetItemDto;
import team.closetalk.closet.service.ClosetItemService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/closet")
public class ClosetItemController {
    private final ClosetItemService closetItemService;

    @PostMapping("{nickname}/{closetName}/{itemId}")
    public ClosetItemDto closetItemRead(@PathVariable("nickname") String nickname,
                                        @PathVariable("closetName") String closetName,
                                        @PathVariable("itemId") Long itemId,
                                        Authentication authentication) {
        return closetItemService.readClosetItem(nickname, closetName, itemId, authentication);
    }

    @PutMapping("/item/{itemId}")
    public void closetItemModify(@PathVariable("itemId") Long itemId,
                                 @RequestParam Map<String, String> itemParams,
                                 Authentication authentication) {
        closetItemService.modifyClosetItem(itemId, itemParams, authentication);
    }

    @DeleteMapping("/item/{itemId}")
    public void closetItemDelete(@PathVariable("itemId") Long itemId,
                                 Authentication authentication) {
        closetItemService.deleteClosetItem(itemId, authentication);
    }
}
