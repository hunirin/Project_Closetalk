package team.closetalk.ootd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
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

    @PostMapping()
    public Long writeOotdArticle(Authentication authentication
            , @RequestParam("image_list") List<MultipartFile> imageList
            , @RequestParam("content") String content
            , @RequestParam("hashtag") String hashtag
    ){
        //imageList에 유효한 값이 하나도 없는 경우 예외처리.
        if(imageList.get(0).isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        OotdArticleDto ootdArticleDto = new OotdArticleDto();
        ootdArticleDto.setContent(content);
        ootdArticleDto.setHashtag(hashtag);

        return ootdArticleService.createOotdArticle(authentication, ootdArticleDto, imageList);
    }

    @ResponseBody
    @PutMapping("/{articleId}")
    public Long updateOotdArticle(Authentication authentication
            , @PathVariable("articleId") Long articleId
            , @RequestParam("content") String content
            , @RequestParam("hashtag") String hashtag
    ){
        OotdArticleDto ootdArticleDto = new OotdArticleDto();
        ootdArticleDto.setContent(content);
        ootdArticleDto.setHashtag(hashtag);
        return ootdArticleService.updateOotdArticle(authentication, articleId, ootdArticleDto).getId();

    }
    @ResponseBody
    @DeleteMapping("/{articleId}")
    public String deleteOotdArticle(Authentication authentication
            , @PathVariable("articleId") Long articleId
    ){
        ootdArticleService.deleteOotdArticle(authentication, articleId);
        return "Delete success";
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
