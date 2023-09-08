package team.closetalk.issue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.issue.entity.IssueArticleEntity;
import team.closetalk.issue.entity.IssueArticleImageEntity;
import team.closetalk.issue.repository.IssueArticleImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueArticleSaveImageService {
    private final IssueArticleImageRepository issueArticleImageRepository;

    // 이미지 저장
    public List<IssueArticleImageEntity> saveArticleImage(IssueArticleEntity article,
                                 List<MultipartFile> articleImages) {
        // 디렉토리 생성
        String ARTICLE_IMAGE_DIRECTORY =
                String.format("src/main/resources/static/images/issue/%d", article.getId());
        try {
            Files.createDirectories(Path.of(ARTICLE_IMAGE_DIRECTORY));
        } catch (IOException e) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 이미지 저장용 리스트
        List<IssueArticleImageEntity> savedImages = new ArrayList<>();

        for (MultipartFile image : articleImages) {
            String imageFileName = image.getOriginalFilename();
            assert imageFileName != null;
            String imageFilePath = Path.of(ARTICLE_IMAGE_DIRECTORY, imageFileName).toString();

            try {
                image.transferTo(Path.of(imageFilePath));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // 이미지 리스트에 저장
            IssueArticleImageEntity savedImage = issueArticleImageRepository.save(new IssueArticleImageEntity(article, imageFilePath));
            savedImages.add(savedImage);
        }
        return savedImages;
    }
}
