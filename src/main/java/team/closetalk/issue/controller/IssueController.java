package team.closetalk.issue.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team.closetalk.issue.dto.IssueArticleDto;
import team.closetalk.issue.service.IssueService;

@Controller
@RequestMapping("/ootd")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;

    @GetMapping("/main")
    public String getIssue(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "12") Integer limit
    ) {
        Page<IssueArticleDto> issuePage = issueService.readLastThreeIssues();
        model.addAttribute("issuePage", issuePage);

        return "ootd/main";
    }
}
