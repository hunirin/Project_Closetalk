package team.closetalk.closet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.closetalk.closet.dto.ClosetItemDto;
import team.closetalk.closet.entity.ClosetItemEntity;
import team.closetalk.closet.service.ClosetItemService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/closet")
public class ClosetItemController {
    private final ClosetItemService closetItemService;

    // 옷장 아이템 조회
    @GetMapping("{nickname}/{closetName}/{itemId}")
    public ClosetItemDto readClosetItem(@PathVariable("nickname") String nickname,
                                        @PathVariable("closetName") String closetName,
                                        @PathVariable("itemId") Long itemId,
                                        Authentication authentication) {
        return closetItemService.readClosetItem(nickname, closetName, itemId, authentication);
    }

    // 옷장 아이템 등록
    @PostMapping("/item/{closetName}")
    public void createClosetItem (
            @PathVariable("closetName") String closetName,
            @RequestPart(value = "data") ClosetItemEntity entity,
            @RequestParam(value = "itemImageUrl") MultipartFile itemImageUrl,
            Authentication authentication
    ) {
        closetItemService.createClosetItem(closetName, entity, itemImageUrl, authentication);
    }

    // 옷장 아이템 수정
    @PutMapping("/item/{itemId}")
    public void updateClosetItem(@PathVariable("itemId") Long itemId,
                                 @RequestParam Map<String, String> itemParams,
                                 Authentication authentication) {
        closetItemService.updateClosetItem(itemId, itemParams, authentication);
    }

    // 옷장 아이템 삭제
    @DeleteMapping("/item/{itemId}")
    public void deleteClosetItem(@PathVariable("itemId") Long itemId,
                                 Authentication authentication) {
        closetItemService.deleteClosetItem(itemId, authentication);
    }
}
