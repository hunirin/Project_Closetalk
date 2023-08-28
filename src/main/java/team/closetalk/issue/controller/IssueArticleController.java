package team.closetalk.issue.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.closetalk.issue.dto.IssueArticleDto;
import team.closetalk.issue.dto.ResponseDto;
import team.closetalk.issue.service.IssueArticleService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/issue")
@RequiredArgsConstructor
public class IssueArticleController {
    private final IssueArticleService service;

    // POST /issue/create
    @PostMapping("/create")
    @ResponseBody
    public IssueArticleDto create(
            @RequestBody IssueArticleDto dto
    ) {
        service.createIssueArticle(dto);
        dto.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        return dto;
    }

    // 이미지 업로드
    @PostMapping(
            value = "/multipart",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto multipart(
            @RequestParam("name") String name,
            @RequestParam("photo") List<MultipartFile> multipartFiles
    ) throws IOException {
        // 저장할 경로
        Files.createDirectories(Path.of("issue"));
        for (MultipartFile multipartFile : multipartFiles) {
            // 업로드하는 파일 이름
            String originalFilename = multipartFile.getOriginalFilename();
            // 저장할 파일이름을 경로에 지정
//        Path path = Path.of("issue/filename.jpg");
            Path path = Path.of("src/main/resources/static/images/issue/", originalFilename);
            // 저장
            multipartFile.transferTo(path);
        }
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("이미지가 추가되었습니다.");
        return responseDto;
    }

//    @GetMapping("/main")
//    public String getIssues() {
//        return "issue";
//    }

    // GET /issue
    @GetMapping
    @ResponseBody
    public Page<IssueArticleDto> readAll() {
        return service.readIssueArticleAll(0, 8);
    }

    // GET /issue/{id}
    @GetMapping("/{id}")
    @ResponseBody
    public IssueArticleDto read(@PathVariable("id") Long id) {
        return service.readIssueArticle(id);
    }

    // PUT /issue/{id}
    @PutMapping("/{id}")
    @ResponseBody
    public IssueArticleDto update(
            @PathVariable("id") Long id,
            @RequestBody IssueArticleDto dto
    ) {
        dto.setModifiedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        return service.updateIssueArticle(id, dto);
    }

    // DELETE /issue/{id}
    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(
            @PathVariable("id") Long id) {
        service.deleteIssueArticle(id);
    }

    // 수정 필요
    @GetMapping("/issueMain")
    public String getIssues(Model model) {
        Page<IssueArticleDto> issueArticlePage = service.readIssueArticleAll(0, 8);
        model.addAttribute("issueList", issueArticlePage.getContent());
        return "issueMain";
    }
}
