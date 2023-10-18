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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class IssueArticleSaveImageService {
    private final IssueArticleImageRepository issueArticleImageRepository;

    private final String ROOT_DIRECTORY = "C:\\Users\\KYS\\git\\Project_Closetalk\\src\\main\\resources";
    private final String ISSUE_IMAGE_DB_PATH = "\\static\\images\\issue\\";

    // 이미지 저장
    public List<IssueArticleImageEntity> saveArticleImage(IssueArticleEntity article,
                                                          List<MultipartFile> imageUrlList) {
        // 디렉토리 생성
        String ARTICLE_IMAGE_DIRECTORY = ROOT_DIRECTORY + ISSUE_IMAGE_DB_PATH + article.getUserId().getId();

        try {
            Files.createDirectories(Path.of(ARTICLE_IMAGE_DIRECTORY));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String imagePath = ISSUE_IMAGE_DB_PATH + article.getUserId().getId();
        // 이미지 저장용 리스트
        List<IssueArticleImageEntity> issueImages = new ArrayList<>();

        for (MultipartFile image : imageUrlList) {

            String originalFilename = image.getOriginalFilename();

            if (originalFilename != null) {
                String[] originalFilenameSplit = originalFilename.split("\\.");
                String extension = originalFilenameSplit[originalFilenameSplit.length - 1];

                // UUID 생성
                String uuid = UUID.randomUUID().toString();

                // 새로운 이미지 파일 이름 생성
                String imageFileName = originalFilename.replaceFirst("\\..*", "") + "_" + uuid + "." + extension;

                // 이미지 파일 경로 생성
                String imageFilePath = Path.of(imagePath, imageFileName).toString();

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
        }

        return issueImages;
    }
}

