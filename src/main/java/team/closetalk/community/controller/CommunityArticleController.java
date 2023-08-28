package team.closetalk.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team.closetalk.community.dto.CommunityArticleDto;
import team.closetalk.community.service.CommunityArticleService;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityArticleController {
    private final CommunityArticleService communityArticleService;

    /* 게시글 기능 */

    @GetMapping("/main")
    public String mainPage() {
        return "community/main";
    }

    // 게시글 목록 조회
    @GetMapping("/list")
    public Page<CommunityArticleDto> readAll(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        Page<CommunityArticleDto> communityPage = communityArticleService.readCommunityPaged(page, limit);
        model.addAttribute("communityPage", communityPage);

        return communityArticleService.readCommunityPaged(page, limit);
    }

    // 게시글 상세 조회
    @GetMapping("/{articleId}")
    public CommunityArticleDto readOne(
            @PathVariable Long articleId
    ) {
        return communityArticleService.readArticleOne(articleId);
    }

    // 게시글 삭제
    @DeleteMapping("/{articleId}")
    public void deleteArticle(
            @PathVariable Long articleId
    ) {
        communityArticleService.deleteArticle(articleId);
    }
}
