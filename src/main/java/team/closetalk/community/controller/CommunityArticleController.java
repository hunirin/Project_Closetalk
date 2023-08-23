package team.closetalk.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team.closetalk.community.dto.CommunityArticleDto;
import team.closetalk.community.service.CommunityArticleService;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityArticleController {
    private final CommunityArticleService communityService;

    @GetMapping("/main")
    public String mainPage() {
        return "community/main";
    }


    @GetMapping("/list")
    public Page<CommunityArticleDto> readAll(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        Page<CommunityArticleDto> communityPage = communityService.readCommunityPaged(page, limit);
        model.addAttribute("communityPage", communityPage);

        return communityService.readCommunityPaged(page, limit);
    }

    @GetMapping("/{articleId}")
    public CommunityArticleDto readOne(
            @PathVariable Long articleId
    ) {
        return communityService.readArticleOne(articleId);
    }

}
