package team.closetalk.closet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String viewClosetList() {
        return "/closet/itemRegistration";
    }

    // 옷장 아이템 등록
    @PostMapping("/item")
    public String createClosetItem (
            Model model,
            @RequestParam(value = "closetName") String closetName,
            @ModelAttribute(value = "data") ClosetItemEntity entity,
            @RequestParam(value = "itemImageUrl") MultipartFile itemImageUrl,
            Authentication authentication
    ) {

        closetItemService.createClosetItem(closetName, entity, itemImageUrl, authentication);
        return "/closet/itemRegistration";
    }
}
