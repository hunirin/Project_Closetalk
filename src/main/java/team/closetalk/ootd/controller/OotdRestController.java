package team.closetalk.ootd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team.closetalk.ootd.dto.OotdArticleDto;
import team.closetalk.ootd.dto.OotdCommentDto;
import team.closetalk.ootd.service.OotdArticleService;
import team.closetalk.ootd.service.OotdCommentService;

import java.util.List;

@RestController
@RequestMapping("/ootd")
@RequiredArgsConstructor
public class OotdRestController {
    private final OotdArticleService ootdArticleService;
    private final OotdCommentService ootdCommentService;

    @GetMapping("/rest/{articleId}")
    public OotdArticleDto readOotdOneRest(
            @PathVariable Long articleId
    ) {
        OotdArticleDto ootdArticle = ootdArticleService.readOotdOne(articleId);
        return ootdArticle;
    }

//    @GetMapping("/{articleId}/comment")
//    public List<OotdCommentDto> commentList(
//            @PathVariable Long articleId
//    ) {
//        return ootdCommentService.readCommentList(articleId);
//    }
//
//    // 댓글 생성
//    @PostMapping("/{articleId}")
//    public void createComment(
//            @PathVariable("articleId") Long articleId,
//            @RequestParam("content") String content,
//            Authentication authentication
//    ) {
//        ootdCommentService.createComment(articleId, content, authentication);
//    }
}
