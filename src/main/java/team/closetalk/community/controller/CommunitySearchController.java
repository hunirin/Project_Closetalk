package team.closetalk.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.closetalk.community.dto.article.response.CommunityArticleListDto;
import team.closetalk.community.service.CommunitySearchService;

@RestController
@RequestMapping("/community/search")
@RequiredArgsConstructor
public class CommunitySearchController {
    private final CommunitySearchService communitySearchService;
    // 전체 검색
    @GetMapping
    public Page<CommunityArticleListDto> readListBySearchAll(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return communitySearchService.searchCommunityPaged(keyword, page, limit);
    }

    // 제목 검색
    @GetMapping("/title")
    public Page<CommunityArticleListDto> readListBySearchTitle(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return communitySearchService.searchTitleCommunityPaged(keyword, page, limit);
    }

    // 닉네임 검색
    @GetMapping("/nickname")
    public Page<CommunityArticleListDto> readListBySearchNickname(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return communitySearchService.searchNicknameCommunityPaged(keyword, page, limit);
    }
}
