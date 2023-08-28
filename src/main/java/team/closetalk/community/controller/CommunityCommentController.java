package team.closetalk.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import team.closetalk.community.service.CommunityCommentService;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityCommentController {
    private final CommunityCommentService communityCommentService;

    // 댓글 생성
    @PostMapping("/{articleId}")
    public void commentCreate(@PathVariable("articleId") Long articleId,
                              @RequestParam("content") String content,
                              Authentication authentication) {
        communityCommentService.createComment(articleId, content, authentication);
    }

    // 대댓글 생성
    @PostMapping("/{articleId}/{commentId}")
    public void replyCreate(@PathVariable Long articleId,
                            @PathVariable Long commentId,
                            Authentication authentication) {
        communityCommentService.createReply(articleId, commentId, authentication);
    }

    @PutMapping("/{articleId}/{commentId}")
    public void commentUpdate(@PathVariable Long articleId,
                              @PathVariable Long commentId,
                              Authentication authentication) {
        communityCommentService.updateComment(articleId, commentId, authentication);
    }

    // 댓글 삭제
    @DeleteMapping("/{articleId}/{commentId}")
    public void deleteComment(@PathVariable Long articleId,
                              @PathVariable Long commentId,
                              Authentication authentication) {
        communityCommentService.deleteComment(articleId, commentId, authentication);
    }
}
