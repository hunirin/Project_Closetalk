package team.closetalk.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team.closetalk.community.dto.article.response.CommunityArticleListDto;
import team.closetalk.mypage.service.LikedArticleService;
import team.closetalk.ootd.dto.OotdArticleDto;


@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class LikeArticleController {
    private final LikedArticleService likedArticleService;

    @GetMapping("/liked/community")
    public String likedCommPage() {
        return "mypage/likedCommunityList";
    }

    @GetMapping("/liked/ootd")
    public String likedOotdPage() {
        return "mypage/likedOotdList";
    }

    // Community
    // 좋아요 한 목록
    @GetMapping("/liked/community/list")
    public String readCommunityArticleList(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            Authentication authentication
    ) {
        Page<CommunityArticleListDto> likedCommunityPage = likedArticleService.readLikedCommunityArticlePaged(page, limit, authentication);
        model.addAttribute("likedCommunityPage", likedCommunityPage);

        return "mypage/likedCommunityList";

    }

    // OOTD
    // 좋아요 한 목록
    @GetMapping("/liked/ootd/list")
    public String readOotdArticleList(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            Authentication authentication
    ) {
        Page<OotdArticleDto> likedOotdPage = likedArticleService.readLikedOotdArticlePaged(page, limit, authentication);
        model.addAttribute("likedOotdPage", likedOotdPage);

        return "mypage/likedOotdList";
    }
}