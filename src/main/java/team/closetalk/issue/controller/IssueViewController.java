package team.closetalk.issue.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team.closetalk.issue.dto.IssueArticleDto;
import team.closetalk.issue.dto.IssueArticleListDto;
import team.closetalk.issue.enumeration.Category;
import team.closetalk.issue.service.IssueArticleSearchService;
import team.closetalk.issue.service.IssueArticleService;

@Controller
@RequestMapping("/issue/view")
@RequiredArgsConstructor
public class IssueViewController {
    private final IssueArticleService issueArticleService;
    private final IssueArticleSearchService issueArticleSearchService;

    @GetMapping("/list")
    public String getIssues(
            @RequestParam(value = "category", defaultValue = "ALL") Category category,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            Model model) {

        Page<IssueArticleListDto> issueList;

        if (category != Category.ALL) {
            issueList = issueArticleService.readIssuePagedByCategory(category, page, limit);
        } else {
            issueList = issueArticleService.readIssuePaged(page, limit);
        }

        model.addAttribute("issueList", issueList);
        model.addAttribute("category", category);

        return "/issue/issueList";
    }

    @GetMapping("/header")
    public String header() {
        return "header";
    }

    @GetMapping("/search")
    public String searchIssue(
            @RequestParam(value = "searchType", defaultValue = "all") String searchType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            Model model
    ) {
        Page<IssueArticleListDto> issueList;

        if ("title".equals(searchType)) {
            issueList = issueArticleSearchService.searchTitleIssuePaged(keyword, page, limit);
        } else if ("nickname".equals(searchType)) {
            issueList = issueArticleSearchService.searchNicknameIssuePaged(keyword, page, limit);
        } else {
            issueList = issueArticleSearchService.searchIssuePaged(keyword, page, limit);
        }

        model.addAttribute("issueList", issueList);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);

        return "issue/issueList";
    }

    @GetMapping("/{articleId}")
    public String readArticle(
            @PathVariable Long articleId,
            Model model
    ) {
        IssueArticleDto issueArticle = issueArticleService.readArticle(articleId);
        model.addAttribute("issueArticle", issueArticle);
        model.addAttribute("articleId", articleId);

        return "issue/issueArticle";
    }
}
