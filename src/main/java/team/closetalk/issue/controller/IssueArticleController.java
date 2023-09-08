package team.closetalk.issue.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.closetalk.issue.enumeration.Category;
import team.closetalk.issue.dto.IssueArticleDto;
import team.closetalk.issue.dto.IssueArticleListDto;
import team.closetalk.issue.dto.IssueCreateArticleDto;
import team.closetalk.issue.service.IssueArticleService;

import java.util.List;

@RestController
@RequestMapping("/issue")
@RequiredArgsConstructor
public class IssueArticleController {
    private final IssueArticleService issueArticleService;


    // 게시글 생성
    @PostMapping(path="/create")
    public IssueArticleDto createArticle(
            @RequestPart(value = "data") IssueCreateArticleDto dto,
            @RequestPart(value = "imageUrl", required = false) List<MultipartFile> imageUrlList,
            Authentication authentication) {
        return issueArticleService.createArticle(dto, imageUrlList, authentication);
    }

    // 게시글 전체 조회
    @GetMapping
    public Page<IssueArticleListDto> readArticleList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ) {
        return issueArticleService.readIssuePaged(page,limit);
    }

    // 카테고리별 게시글 목록 조회
    @GetMapping("/category")
    public Page<IssueArticleListDto> readArticleListByCategory(
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return issueArticleService.readIssuePagedByCategory(category, page, limit);
    }

    // 게시글 상세 조회
    @GetMapping("/{articleId}")
    public IssueArticleDto readArticle(
            @PathVariable Long articleId
    ) {
        return issueArticleService.readArticle(articleId);
    }

    // 게시글 수정
    @PutMapping("/{articleId}")
    public IssueArticleDto updateArticle(
            @PathVariable("articleId") Long articleId,
            @RequestPart(value = "data") IssueArticleDto dto,
            @RequestPart(value = "newImageUrl", required = false) List<MultipartFile> imagesUrlList,
            Authentication authentication
    ) {
        return issueArticleService.updateArticle(articleId, authentication, imagesUrlList, dto);
    }

    // 게시글 삭제
    @DeleteMapping("/{articleId}")
    public void deleteArticle(
            @PathVariable("articleId") Long articleId,
            Authentication authentication
    ) {
        issueArticleService.deleteArticle(articleId, authentication);
    }
}
