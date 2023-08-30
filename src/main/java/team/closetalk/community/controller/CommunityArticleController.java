package team.closetalk.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team.closetalk.community.dto.CommunityArticleDto;
import team.closetalk.community.dto.CommunityArticleListDto;
import team.closetalk.community.service.CommunityArticleService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityArticleController {
    private final CommunityArticleService communityArticleService;

    // 게시글 목록 조회
    @GetMapping
    public Page<CommunityArticleListDto> readAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return communityArticleService.readCommunityPaged(page, limit);
    }

    // 게시글 상세 조회
    @GetMapping("/{articleId}")
    public CommunityArticleDto readOne(
            @PathVariable Long articleId
    ) {
        return communityArticleService.readArticleOne(articleId);
    }

    // 게시글 수정
    @PutMapping("/{articleId}")
    public CommunityArticleDto update(
            @PathVariable Long articleId,
            @RequestBody CommunityArticleDto dto,
            Authentication authentication
    ) {
        return communityArticleService.updateCommunityArticle(articleId, authentication, dto);
    }

    // 게시글 삭제
    @DeleteMapping("/{articleId}")
    public void deleteArticle(
            @PathVariable Long articleId,
            Authentication authentication
    ) {
        communityArticleService.deleteArticle(articleId, authentication);
    }
}
