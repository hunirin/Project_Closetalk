package team.closetalk.ootd.controller;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.realm.AuthenticatedUserRealm;
import org.springframework.data.domain.Page;
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
import team.closetalk.ootd.service.OotdLikeService;

import java.util.List;

@RestController
@RequestMapping("/ootd")
@RequiredArgsConstructor
public class OotdRestController {
    private final OotdArticleService ootdArticleService;
    private final OotdCommentService ootdCommentService;
    private final OotdLikeService ootdLikeService;

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
            , @RequestBody OotdArticleDto ootdArticleDto
    ){
        System.out.println(ootdArticleDto.toString());
//        OotdArticleDto ootdArticleDto = new OotdArticleDto();
//        ootdArticleDto.setContent(content);
//        ootdArticleDto.setHashtag(hashtag);
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

    // 게시글 좋아요
    @PostMapping("/like/{articleId}")
    public String likeOotdArticle(
            @PathVariable Long articleId,
            Authentication authentication
    ) {
        return ootdLikeService.likeOotdArticle(articleId, authentication);
    }

    @GetMapping("/rest/list")
    public Page<OotdArticleDto> readOotList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "12") Integer limit
    ) {
        Page<OotdArticleDto> ootdPage = ootdArticleService.readOotdPaged(page, limit);

        return ootdPage;
    }
}
