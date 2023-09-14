package team.closetalk.issue.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.closetalk.issue.dto.IssueArticleListDto;
import team.closetalk.issue.service.IssueArticleSearchService;

@RestController
@RequestMapping("/issue/search")
@RequiredArgsConstructor
public class IssueArticleSearchController {
    private final IssueArticleSearchService issueArticleSearchService;

    // 전체 검색
    @GetMapping
    public Page<IssueArticleListDto> readListBySearchAll(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return issueArticleSearchService.searchIssuePaged(keyword, page, limit);
    }

    // 제목 검색
    @GetMapping("/title")
    public Page<IssueArticleListDto> readListBySearchTitle(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return issueArticleSearchService.searchNicknameIssuePaged(keyword, page, limit);
    }

    // 닉네임 검색
    @GetMapping("/nickname")
    public Page<IssueArticleListDto> readListBySearchNickname(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return issueArticleSearchService.searchNicknameIssuePaged(keyword, page, limit);
    }
}
