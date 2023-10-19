package team.closetalk.issue.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.issue.entity.IssueArticleEntity;
import team.closetalk.issue.entity.IssueArticleImageEntity;
import team.closetalk.issue.repository.IssueArticleImageRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class IssueArticleSaveImageService {
    private final IssueArticleImageRepository issueArticleImageRepository;

    private final String ROOT_DIRECTORY = "src/main/resources";
    private final String ISSUE_IMAGE_DB_PATH = "/static/images/issue/";

    // 이미지 저장
    public List<IssueArticleImageEntity> saveArticleImage(IssueArticleEntity article,
                                                          List<MultipartFile> imageUrlList) {
        // 디렉토리 생성
        String ARTICLE_IMAGE_DIRECTORY = ROOT_DIRECTORY + ISSUE_IMAGE_DB_PATH + article.getId();
        ARTICLE_IMAGE_DIRECTORY = ARTICLE_IMAGE_DIRECTORY.replace("\\", "/"); // 역슬래시를 슬래시로 치환

        try {
            Files.createDirectories(Path.of(ARTICLE_IMAGE_DIRECTORY));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String imagePath = ISSUE_IMAGE_DB_PATH + article.getId();
        // 이미지 저장용 리스트
        List<IssueArticleImageEntity> issueImages = new ArrayList<>();

        for (MultipartFile image : imageUrlList) {

            String originalFilename = image.getOriginalFilename();

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String timestamp = now.format(formatter);

            // 원본 파일 이름에서 확장자 추출
            String originalFileName = image.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));


            // 새로운 이미지 파일 이름 생성
            String imageFileName = timestamp + fileExtension;

            // 이미지 파일 경로 생성
            String imageFilePath = Path.of(imagePath, imageFileName).toString();
            imageFilePath = imageFilePath.replace("\\", "/"); // 역슬래시를 슬래시로 치환

            try {
                image.transferTo(Path.of(ROOT_DIRECTORY + imageFilePath));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            log.info("이미지 경로 : {}", imageFilePath);

            // 이미지 리스트에 저장
            IssueArticleImageEntity savedImage = issueArticleImageRepository.save(new IssueArticleImageEntity(article, imageFilePath));
            log.info("issueImage : {}", savedImage);
            issueImages.add(savedImage);
            log.info("issueImages : {}", issueImages);
        }

        return issueImages;
    }
}

