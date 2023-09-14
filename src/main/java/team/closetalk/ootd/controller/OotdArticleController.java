package team.closetalk.ootd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team.closetalk.ootd.dto.OotdArticleDto;
import team.closetalk.ootd.dto.OotdCommentDto;
import team.closetalk.ootd.service.OotdArticleService;
import team.closetalk.ootd.service.OotdCommentService;

import java.util.List;


@Controller
@RequestMapping("/ootd")
@RequiredArgsConstructor
public class OotdArticleController {
    private final OotdArticleService ootdArticleService;
    private final OotdCommentService ootdCommentService;

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
    public String readOotList(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "12") Integer limit
    ) {
        Page<OotdArticleDto> ootdPage = ootdArticleService.readOotdPaged(page, limit);
        model.addAttribute("ootdPage", ootdPage);

        return "ootd/ootdMain";
    }

    // 게시글 상세 조회
    @GetMapping("/{articleId}")
    public String readOotdOne(
            Model model,
            @PathVariable Long articleId
    ) {
        OotdArticleDto ootdArticle = ootdArticleService.readOotdOne(articleId);
        model.addAttribute("ootdArticle", ootdArticle);
        // 댓글 조회
        List<OotdCommentDto> ootdCommentList = ootdCommentService.readCommentList(articleId);
        model.addAttribute("ootdCommentList", ootdCommentList);
        return "ootd/ootdArticle";
    }

    @GetMapping("/writePage")
    public String writeOotdPage(Model model){
        return "ootd/writeOotdArticle";
    }
}
