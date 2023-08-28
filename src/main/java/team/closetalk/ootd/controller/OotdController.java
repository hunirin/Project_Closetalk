package team.closetalk.ootd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team.closetalk.ootd.dto.OotdArticleDto;
import team.closetalk.ootd.service.OotdService;


@Controller
@RequestMapping("/ootd")
@RequiredArgsConstructor
public class OotdController {
    private final OotdService ootdService;

    @GetMapping
    public String mainPage() {
        return "ootd/ootdMain";
    }

    // 헤더 불러오는 주소
    @GetMapping("/header")
    public String mainHeader() {
        return "header";
    }

     // 메인페이지 무한 스크롤 조회
    @GetMapping("/list")
    public String readOotdMain(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "12") Integer limit
    ) {
        Page<OotdArticleDto> ootdPage = ootdService.readOotdPaged(page, limit);
        model.addAttribute("ootdPage", ootdPage);

        return "ootd/ootdMain";
    }

    @GetMapping("/{articleId}")
    public String readOotdOne(
            Model model,
            @PathVariable Long articleId
    ) {
        OotdArticleDto ootdArticle = ootdService.readOotdOne(articleId);
        model.addAttribute("ootdArticle", ootdArticle);
        return "ootd/ootdArticle";
    }
}
