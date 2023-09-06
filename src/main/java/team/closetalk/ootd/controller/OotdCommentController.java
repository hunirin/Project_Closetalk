package team.closetalk.ootd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team.closetalk.ootd.dto.OotdCommentDto;
import team.closetalk.ootd.service.OotdCommentService;

import java.util.List;

@Controller
@RequestMapping("/ootd")
@RequiredArgsConstructor
public class OotdCommentController {
    private final OotdCommentService ootdCommentService;

    // 댓글 생성
    @PostMapping("/{articleId}")
    public String createComment(
            Model model,
            @PathVariable("articleId") Long articleId,
            @RequestParam("content") String content,
            Authentication authentication
    ) {
        OotdCommentDto ootdComment = ootdCommentService.createComment(articleId, content, authentication);
        model.addAttribute("ootdComment", ootdComment);
        return "redirect:/ootd/" + articleId;
    }

    // 대댓글 생성
    @PostMapping("/{articleId}/{commentId}")
    public String replyCreate(
            Model model,
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @RequestParam("content") String content,
            Authentication authentication
    ) {
        OotdCommentDto ootdComment = ootdCommentService.createReply(articleId, commentId, content, authentication);
        model.addAttribute("ootdComment", ootdComment);
        return "redirect:/ootd/" + articleId;
    }

    // 댓글 수정
    @PutMapping("/{articleId}/{commentId}")
    public String commentUpdate(
            Model model,
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @RequestParam("content") String content,
            Authentication authentication
    ) {
        OotdCommentDto ootdComment = ootdCommentService.updateComment(articleId, commentId, content, authentication);
        model.addAttribute("ootdComment", ootdComment);
        return "redirect:/ootd/" + articleId;
    }

    // 댓글 조회
    @GetMapping("/{articleId}/comment")
    public String commentList(
            Model model,
            @PathVariable Long articleId
    ) {
        List<OotdCommentDto> ootdCommentList = ootdCommentService.readCommentList(articleId);
        model.addAttribute("ootdCommentList", ootdCommentList);
        model.addAttribute("articleId", articleId);
        return "ootd/ootdComment";
    }


    // 댓글 삭제
    @DeleteMapping("/{articleId}/{commentId}")
    public String deleteComment(
            Model model,
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            Authentication authentication
    ) {
        OotdCommentDto ootdComment = ootdCommentService.deleteComment(articleId, commentId, authentication);
        model.addAttribute("ootdComment", ootdComment);
        return "redirect:/ootd/" + articleId;
    }
}
