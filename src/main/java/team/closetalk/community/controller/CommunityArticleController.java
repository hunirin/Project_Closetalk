package team.closetalk.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.closetalk.community.dto.article.response.CommunityArticleDto;
import team.closetalk.community.dto.article.response.CommunityArticleListDto;
import team.closetalk.community.dto.article.request.CommunityCreateArticleDto;
import team.closetalk.community.enumeration.Category;
import team.closetalk.community.service.CommunityArticleService;
import team.closetalk.community.service.CommunityLikeService;

import java.util.List;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityArticleController {
    private final CommunityArticleService communityArticleService;
    private final CommunityLikeService communityLikeService;

    // 게시글 목록 조회
    @GetMapping
    public Page<CommunityArticleListDto> readArticleList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return communityArticleService.readCommunityPaged(page, limit);
    }
    // 카테고리별 게시글 목록 조회
    @GetMapping("/category")
    public Page<CommunityArticleListDto> readArticleListByCategory(
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return communityArticleService.readCommunityPagedByCategory(category, page, limit);
    }
    // 게시글 상세 조회
    @GetMapping("/{articleId}")
    public CommunityArticleDto readArticle(
            @PathVariable Long articleId
    ) {
        return communityArticleService.readArticle(articleId);
    }

    // 게시글 수정
    @PutMapping("/{articleId}")
    public CommunityArticleDto updateArticle(
            @PathVariable Long articleId,
            @RequestBody CommunityArticleDto dto,
            Authentication authentication
    ) {
        return communityArticleService.updateArticle(articleId, authentication, dto);
    }

    // 게시글 삭제
    @DeleteMapping("/{articleId}")
    public void deleteArticle(
            @PathVariable Long articleId,
            Authentication authentication
    ) {
        communityArticleService.deleteArticle(articleId, authentication);
    }

    // 게시글 생성 (COMMUNITY)
    @PostMapping("/create")
    public CommunityArticleDto createArticle(@RequestPart(value = "data") CommunityCreateArticleDto dto,
                                             @RequestPart(value = "imageUrl",
                                                     required = false) List<MultipartFile> imageUrlList,
                                             Authentication authentication) {
        return communityArticleService.createArticle(dto, imageUrlList, authentication);
    }

    // 게시글 좋아요
    @PostMapping("/like/{articleId}")
    public String likeCommunityArticle(@PathVariable Long articleId, Authentication authentication) {
        return communityLikeService.likeCommunityArticle(articleId, authentication);
    }
}
