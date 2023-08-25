package team.closetalk.issue.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import team.closetalk.issue.dto.IssueArticleDto;
import team.closetalk.issue.repository.IssueArticleRepository;
import team.closetalk.issue.service.IssueArticleService;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Controller
@RequiredArgsConstructor
public class IssueViewController {
    private final IssueArticleService issueArticleService;
    private final IssueArticleRepository issueArticleRepository;

    @GetMapping("/issue/view")
    public String issueArticleList(
            @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "limit", defaultValue = "8") Integer pageSize,
            Model model) {
        Page<IssueArticleDto> issuePage = issueArticleService.readIssueArticleAll(pageNumber, pageSize);
        model.addAttribute("issueArticleEntity", issuePage.getContent());
        model.addAttribute("issuePage", issuePage);
        return "issueMain";
    }

    @GetMapping("/issue/view/{id}")
    public String issueArticleRead(
            @PathVariable("id") Long id,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            Model model) {
        IssueArticleDto issueArticleDto = issueArticleService.readIssueArticle(id);

        model.addAttribute("issueArticleEntity", issueArticleDto.getContent());
        model.addAttribute("id", id);
        return "issueDetail";
    }

    @PostMapping("/issue/view/create")
    public String createIssue(
            @RequestParam("IssueTitle") String title,
            @RequestParam("IssueContent") String content,
            @RequestParam(value = "imageUrl", required = false) MultipartFile imageUrl) {
        IssueArticleDto dto = new IssueArticleDto();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        return "redirect:/issueMain";
    }


}
