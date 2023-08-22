package team.closetalk.closet.controller;

import lombok.RequiredArgsConstructor;
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
    public ClosetItemDto closetItemRead(@PathVariable("itemId") Long itemId) {
        return closetItemService.readClosetItem(itemId);
    }

    @PutMapping("/{itemId}")
    public void closetItemModify(@PathVariable("itemId") Long itemId,
                                 @RequestParam Map<String, String> itemParams) {
        closetItemService.modifyClosetItem(itemId, itemParams);
    }

    @DeleteMapping("/{itemId}")
    public void closetItemDelete(@PathVariable("itemId") Long itemId) {
        closetItemService.deleteClosetItem(itemId);
    }
}
