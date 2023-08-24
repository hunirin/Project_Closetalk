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
        Page<CommunityArticleDto> communityPage = communityService.readCommunityPaged(page, limit);
        model.addAttribute("communityPage", communityPage);

        return communityService.readCommunityPaged(page, limit);
    }

    // 게시글 상세 조회
    @GetMapping("/{articleId}")
    public CommunityArticleDto readOne(
            @PathVariable Long articleId
    ) {
        return communityService.readArticleOne(articleId);
    }

    // 게시글 삭제
    @DeleteMapping("/{articleId}")
    public void deleteArticle(
            @PathVariable Long articleId
    ) {
        communityService.deleteArticle(articleId);
    }

    /* 댓글 기능 */

    // 댓글 삭제
    @DeleteMapping("/{articleId}/{commentId}")
    public void deleteComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId
    ) {
        communityService.deleteComment(articleId, commentId);
    }

    // 대댓글 삭제
    @DeleteMapping("/{articleId}/{commentId}/{reCommentId}")
    public void deleteComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @PathVariable Long reCommentId
    ) {
        communityService.deleteReComment(articleId, commentId, reCommentId);
    }
}
