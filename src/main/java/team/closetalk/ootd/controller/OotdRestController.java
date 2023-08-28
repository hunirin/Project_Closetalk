package team.closetalk.ootd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.closetalk.ootd.dto.OotdArticleDto;
import team.closetalk.ootd.service.OotdService;

@RestController
@RequestMapping("/ootd")
@RequiredArgsConstructor
public class OotdRestController {
    private final OotdService ootdService;

    @GetMapping("/rest/{articleId}")
    public OotdArticleDto readOotdOneRest(
            @PathVariable Long articleId
    ) {
        OotdArticleDto ootdArticle = ootdService.readOotdOne(articleId);
        return ootdArticle;
    }
}
