package team.closetalk.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import team.closetalk.community.dto.CommunityCommentDto;
import team.closetalk.community.service.CommunityCommentService;

import java.util.List;

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
                            @RequestParam("content") String content,
                            Authentication authentication) {
        communityCommentService.createReply(articleId, commentId, content, authentication);
    }

    // 댓글 수정
    @PutMapping("/{articleId}/{commentId}")
    public void commentUpdate(@PathVariable Long articleId,
                              @PathVariable Long commentId,
                              @RequestParam("content") String content,
                              Authentication authentication) {
        communityCommentService.updateComment(articleId, commentId, content, authentication);
    }

    // 댓글 조회
    @PostMapping("/{articleId}/comment")
    public List<CommunityCommentDto> commentList(@PathVariable Long articleId) {
        return communityCommentService.readCommentList(articleId);
    }

    // 댓글 삭제
    @DeleteMapping("/{articleId}/{commentId}")
    public void deleteComment(@PathVariable Long articleId,
                              @PathVariable Long commentId,
                              Authentication authentication) {
        communityCommentService.deleteComment(articleId, commentId, authentication);
    }
}
