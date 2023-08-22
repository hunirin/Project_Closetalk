package team.closetalk.issue.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
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
@RestController
@RequestMapping("/issueArticles")
@RequiredArgsConstructor
public class IssueArticleController {
    private final IssueArticleService service;

    // POST /issueArticles
    @PostMapping
    public IssueArticleDto create(
            @RequestBody IssueArticleDto dto
    ) {
        service.createIssueArticle(dto);
        dto.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        return service.createIssueArticle(dto);
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
//    public String getIssueArticles() {
//        return "issueArticles";
//    }

    // GET /issueArticles
    @GetMapping
    public Page<IssueArticleDto> readAll() {
        return service.readIssueArticleAll(0, 8);
    }

    // GET /issueArticles/{id}
    @GetMapping("/{id}")
    public IssueArticleDto read(@PathVariable("id") Long id) {
        return service.readIssueArticle(id);
    }

    // PUT /issueArticles/{id}
    @PutMapping("/{id}")
    public IssueArticleDto update(
            @PathVariable("id") Long id,
            @RequestBody IssueArticleDto dto
    ) {
        return service.updateIssueArticle(id, dto);
    }

    // DELETE /issueArticles/{id}
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id") Long id) {
        service.deleteIssueArticle(id);
    }
}
