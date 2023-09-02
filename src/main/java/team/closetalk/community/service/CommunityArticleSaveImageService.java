package team.closetalk.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.entity.CommunityArticleImagesEntity;
import team.closetalk.community.repository.CommunityArticleImagesRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityArticleSaveImageService {
    private final CommunityArticleImagesRepository imagesRepository;

    public void saveArticleImage(CommunityArticleEntity article,
                                    List<MultipartFile> articleImages){
        //이미지 저장 디렉토리 생성
        String ARTICLE_IMAGE_DIRECTORY =
                String.format("src/main/resources/static/images/community/%d", article.getId());
        try {
            Files.createDirectories(Path.of(ARTICLE_IMAGE_DIRECTORY));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        for (MultipartFile image : articleImages) {
            String imageFileName = image.getOriginalFilename();
            assert imageFileName != null;
            String imageFilePath = Path.of(ARTICLE_IMAGE_DIRECTORY, imageFileName).toString();

            try {
                image.transferTo(Path.of(imageFilePath));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            imagesRepository.save(new CommunityArticleImagesEntity(article ,imageFilePath));
        }
    }
}
