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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityArticleSaveImageService {
    private final CommunityArticleImagesRepository imagesRepository;
    private final ClosetItemRepository closetItemRepository;
    private final ArticleAndClosetItemRepository articleAndClosetItemRepository;

    // 이미지 저장
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
