package team.closetalk.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.closet.entity.ClosetItemEntity;
import team.closetalk.closet.repository.ClosetItemRepository;
import team.closetalk.community.dto.article.request.CommunityCreateArticleDto;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.entity.CommunityArticleImagesEntity;
import team.closetalk.community.entity.composite.ArticleClosetItemId;
import team.closetalk.community.entity.composite.CommunityArticleClosetItems;
import team.closetalk.community.repository.ArticleAndClosetItemRepository;
import team.closetalk.community.repository.CommunityArticleImagesRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityArticleSaveImageService {
    private final CommunityArticleImagesRepository imagesRepository;
    private final ClosetItemRepository closetItemRepository;
    private final ArticleAndClosetItemRepository articleAndClosetItemRepository;

    private final String ROOT_DIRECTORY = "src/main/resources";

    // 이미지 저장
    public void saveArticleImage(CommunityArticleEntity article, List<MultipartFile> articleImages) {
        // 이미지 저장 디렉토리 생성 (경로 수정)
        String ARTICLE_IMAGE_DIRECTORY = String.format("/static/images/community/%d", article.getId());
        ARTICLE_IMAGE_DIRECTORY = ARTICLE_IMAGE_DIRECTORY.replace("\\", "/"); // 역슬래시를 슬래시로 치환

        try {
            Files.createDirectories(Path.of(ROOT_DIRECTORY + ARTICLE_IMAGE_DIRECTORY));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        for (MultipartFile image : articleImages) {
            // 현재 날짜와 시간을 이용하여 파일 이름 생성
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String timestamp = now.format(formatter);

            // 원본 파일 이름에서 확장자 추출
            String originalFileName = image.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // 새로운 파일 이름 생성 (날짜와 확장자 포함)
            String newFileName = timestamp + fileExtension;

            // 파일 경로 설정
            String imageFilePath = Path.of(ARTICLE_IMAGE_DIRECTORY, newFileName).toString();
            imageFilePath = imageFilePath.replace("\\", "/"); // 역슬래시를 슬래시로 치환

            try {
                image.transferTo(Path.of(ROOT_DIRECTORY + imageFilePath));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            imagesRepository.save(new CommunityArticleImagesEntity(article, imageFilePath));
        }
    }

    // 옷장 아이템 저장
    public void saveArticleWithCloset(CommunityCreateArticleDto dto, CommunityArticleEntity article) {
        List<Long> closetItemNumList = dto.getSelectClosetItemNumList();

        for (Long closetItemNum : closetItemNumList) {
            ClosetItemEntity closetItem = closetItemRepository.findById(closetItemNum)
                    .orElseThrow(() -> {
                        log.error("해당 아이템을 찾을 수 없습니다.");
                        return new ResponseStatusException(HttpStatus.NOT_FOUND);
                    });
            ArticleClosetItemId articleClosetItemId =
                    new ArticleClosetItemId(article.getId(), closetItemNum);
            CommunityArticleClosetItems communityArticleClosetItems =
                    new CommunityArticleClosetItems(articleClosetItemId, article, closetItem);
            articleAndClosetItemRepository.save(communityArticleClosetItems);
        }
    }
}
