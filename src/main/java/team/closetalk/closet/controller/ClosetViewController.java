package team.closetalk.closet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.closetalk.closet.dto.ClosetDto;
import team.closetalk.closet.entity.ClosetItemEntity;
import team.closetalk.closet.service.ClosetItemService;
import team.closetalk.closet.service.ClosetService;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/closet/view")
public class ClosetViewController {
    private final ClosetService closetService;
    private final ClosetItemService closetItemService;

    // 아이템 등록 페이지
    @GetMapping("/item")
    public String closetList(Model model, Authentication authentication) {
        // 옷장 목록
        List<ClosetDto> closetList = closetService.findCloset(authentication);
        model.addAttribute("closetList", closetList);
        return "closet/itemRegistration";
    }

    // 옷장 아이템 등록
    @PostMapping("/item/{closetName}")
    public String createClosetItem (
            Model model,
            @PathVariable("closetName") String closetName,
            @RequestPart(value = "data") ClosetItemEntity entity,
            @RequestParam(value = "itemImageUrl") MultipartFile itemImageUrl,
            Authentication authentication
    ) {
        closetItemService.createClosetItem(closetName, entity, itemImageUrl, authentication);
        model.addAttribute("closetName", closetName);
        return "closet/itemRegistration";
    }
}
