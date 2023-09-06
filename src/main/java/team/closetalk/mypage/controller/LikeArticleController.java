package team.closetalk.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.closetalk.community.dto.article.response.CommunityArticleListDto;
import team.closetalk.mypage.service.LikedArticleService;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class LikeArticleController {
    private final LikedArticleService likedArticleService;

    // 좋아요 한 목록
    @GetMapping("/liked")
    public Page<CommunityArticleListDto> readArticleList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            Authentication authentication
    ) {
        return likedArticleService.readLikedArticlePaged(page, limit, authentication);
    }
}
