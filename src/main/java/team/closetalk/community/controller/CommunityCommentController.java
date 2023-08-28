package team.closetalk.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.closetalk.community.service.CommunityCommentService;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityCommentController {
    private final CommunityCommentService communityCommentService;
    /* 댓글 기능 */

    // 댓글 삭제
    @DeleteMapping("/{articleId}/{commentId}")
    public void deleteComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId
    ) {
        communityCommentService.deleteComment(articleId, commentId);
    }

    // 대댓글 삭제 -- 수정 필요
    @DeleteMapping("/{articleId}/{commentId}/{reCommentId}")
    public void deleteComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @PathVariable Long reCommentId
    ) {
        communityCommentService.deleteReComment(articleId, commentId, reCommentId);
    }
}
