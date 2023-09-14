package team.closetalk.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team.closetalk.community.dto.CommunityCommentDto;
import team.closetalk.community.dto.article.response.CommunityArticleDto;
import team.closetalk.community.dto.article.response.CommunityArticleListDto;
import team.closetalk.community.enumeration.Category;
import team.closetalk.community.service.CommunityArticleService;
import team.closetalk.community.service.CommunityCommentService;
import team.closetalk.community.service.CommunitySearchService;
import team.closetalk.ootd.dto.OotdCommentDto;

import java.util.List;

@Controller
@RequestMapping("/community/view")
@RequiredArgsConstructor
public class CommunityViewController {
    private final CommunityArticleService communityArticleService;
    private final CommunitySearchService communitySearchService;
    private final CommunityCommentService communityCommentService;


    // 게시글 전체 조회
    @GetMapping("/list")
    public String CommunityListPage(
            Model model,
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ) {
        Page<CommunityArticleListDto> communityList;

        if (category != null && category != Category.ALL) {
            // 카테고리별 조회
            communityList = communityArticleService.readCommunityPagedByCategory(category, page, limit);
        } else {
            // 모든 게시글 조회
            communityList = communityArticleService.readCommunityPaged(page, limit);
            category = Category.ALL;
        }

        model.addAttribute("communityList", communityList);
        model.addAttribute("category", category);
        return "community/communityList";
    }

    // 검색기능
    @GetMapping("/search")
    public String searchCommunity(
            @RequestParam(value = "searchType", defaultValue = "all") String searchType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            Model model
    ) {
        Page<CommunityArticleListDto> communityList;

        if ("title".equals(searchType)) {
            communityList = communitySearchService.searchTitleCommunityPaged(keyword, page, limit);
        } else if ("nickname".equals(searchType)) {
            communityList = communitySearchService.searchNicknameCommunityPaged(keyword, page, limit);
        } else {
            communityList = communitySearchService.searchCommunityPaged(keyword, page, limit);
        }

        model.addAttribute("communityList", communityList);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);

        return "community/communityList";
    }

    // 상세페이지 조회
    @GetMapping("/{articleId}")
    public String readArticle(
            Model model,
            @PathVariable Long articleId
    ) {
        CommunityArticleDto communityArticle = communityArticleService.readArticle(articleId);
        model.addAttribute("communityArticle", communityArticle);

        List<CommunityCommentDto> communityCommentList = communityCommentService.readCommentList(articleId);
        model.addAttribute("communityCommentList", communityCommentList);
        model.addAttribute("articleId", articleId);

        return "community/communityArticle";
    }

}
