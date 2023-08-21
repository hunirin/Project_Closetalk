package team.closetalk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.closetalk.dto.IssueArticleDto;
import team.closetalk.service.IssueArticleService;

@Slf4j
@Controller
@RequestMapping("/issueArticles")
@RequiredArgsConstructor
public class IssueArticleController {
    private final IssueArticleService service;

    // POST /IssueArticles
    @PostMapping
    public IssueArticleDto create(
            @RequestBody IssueArticleDto dto
    ) {
        service.createIssueArticle(dto);
        return service.createIssueArticle(dto);
    }

    @GetMapping("/main")
    public String getIssueArticles() {
        return "issueArticles";
    }

    // GET /IssueArticles
    @GetMapping
    public Page<IssueArticleDto> readAll() {
        return service.readIssueArticleAll(0, 8);
    }

    @GetMapping("/{id}")
    public IssueArticleDto read(@PathVariable("id") Long id) {
        return service.readIssueArticle(id);
    }

    // PUT /IssueArticles/{id}
    @PutMapping("/{id}")
    public IssueArticleDto update(
            @PathVariable("id") Long id,
            @RequestBody IssueArticleDto dto
    ) {
        return service.updateIssueArticle(id, dto);
    }

    // DELETE /IssueArticles/{id}
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id") Long id) {
        service.deleteIssueArticle(id);
    }
}
