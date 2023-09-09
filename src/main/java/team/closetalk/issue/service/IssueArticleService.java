package team.closetalk.issue.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.closet.service.EntityRetrievalService;
import team.closetalk.issue.dto.*;
import team.closetalk.issue.entity.IssueArticleEntity;
import team.closetalk.issue.entity.IssueArticleImageEntity;
import team.closetalk.issue.enumeration.Category;
import team.closetalk.issue.repository.IssueArticleImageRepository;
import team.closetalk.issue.repository.IssueArticleRepository;
import team.closetalk.user.entity.UserEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class IssueArticleService {
    private final IssueArticleRepository issueArticleRepository;
    private final IssueArticleImageRepository issueArticleImageRepository;
    private final IssueArticleSaveImageService issueArticleSaveImageService;
    private final EntityRetrievalService entityRetrievalService;

    // 게시글 생성
    public IssueArticleDto createArticle(IssueCreateArticleDto dto,
                                         List<MultipartFile> imageUrlList,
                                         Authentication authentication) {
        UserEntity user = getUserEntity(authentication.getName());
        IssueArticleEntity article =
                new IssueArticleEntity(dto.getCategory(), dto.getTitle(), dto.getContent(), user);
        issueArticleRepository.save(article);
        if (imageUrlList != null) {
            issueArticleSaveImageService.saveArticleImage(article, imageUrlList);
            IssueArticleImageEntity imagesEntityList =
                    issueArticleImageRepository.findAllByIssueArticleId_Id(article.getId()).get(0);
            issueArticleRepository.save(article.saveThumbnail(imagesEntityList.getImageUrl()));

        }
        return readArticle(article.getId());
    }

    // 게시글 상세 페이지 조회
    public IssueArticleDto readArticle(Long articleId) {
        IssueArticleEntity article = issueArticleRepository.findById(articleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (article.getDeletedAt() != null) {
            log.error("삭제된 게시물입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        List<IssueArticleImageEntity> imageEntityList =
                issueArticleImageRepository.findAllByIssueArticleId_Id(article.getId());
        List<IssueArticleImageDto> imageDtoList =
                imageEntityList.stream().map(IssueArticleImageDto::fromEntity).toList();
        // 조회수 증가
        issueArticleRepository.save(article.increaseHit());

        return IssueArticleDto.fromEntity(article, imageDtoList);
    }

    // 게시글 전체 조회
    public Page<IssueArticleListDto> readIssuePaged(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").ascending());

        Page<IssueArticleEntity> issueArticleEntityPage =
                issueArticleRepository.findAllByDeletedAtIsNull(pageable);

        return issueArticleEntityPage.map(IssueArticleListDto::fromEntity);
    }

    // 카테고리별 게시글 조회
    public Page<IssueArticleListDto> readIssuePagedByCategory(Category category,
                                                              Integer pageNum,
                                                              Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").ascending());

        Page<IssueArticleEntity> issueArticleEntityPage =
                issueArticleRepository.findAllByCategoryAndDeletedAtIsNull(category, pageable);
        return issueArticleEntityPage.map(IssueArticleListDto::fromEntity);
    }

    // 게시글 수정
    public IssueArticleDto updateArticle(Long articleId,
                                         Authentication authentication,
                                         List<MultipartFile> newImageUrlList,
                                         IssueArticleDto dto) {
        UserEntity user = getUserEntity(authentication.getName());

        IssueArticleEntity article = issueArticleRepository.findByIdAndUserId_Id(articleId, user.getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setModifiedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        // 게시글 생성시 업로드한 이미지
        List<IssueArticleImageEntity> oldImages = issueArticleImageRepository.findAllByIssueArticleId_Id(articleId);
        String oldThumbnailUrl = article.getThumbnail();

        if (newImageUrlList != null) { // 새로 이미지 추가 시
            // 새로 추가한 이미지를 저장
            List<IssueArticleImageEntity> newImages =
                    issueArticleSaveImageService.saveArticleImage(article, newImageUrlList);
            if (!newImages.isEmpty()){
                // 새로 이미지 추가한 파일의 첫번째 파일을 썸네일로 저장
                String newThumbnailUrl = newImages.get(0).getImageUrl();
                article.setThumbnail(newThumbnailUrl);
            }

            for (IssueArticleImageEntity oldImage : oldImages) {
                issueArticleImageRepository.delete(oldImage);

                if (!oldImage.getImageUrl().equals(oldThumbnailUrl)) { // 기존 이미지와 썸네일의 경로가 다를때
                    try {
                        // 파일 삭제
                        Files.delete(Path.of(oldImage.getImageUrl()));
                    } catch (IOException e) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                    }
                }
            }
      }
        issueArticleRepository.save(article);
        
        return readArticle(articleId);
    }


    public void deleteArticle(Long articleId, Authentication authentication) {
        UserEntity user = getUserEntity(authentication.getName());
        IssueArticleEntity article = issueArticleRepository.findByIdAndUserId_Id(articleId, user.getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!Objects.equals(article.getUserId().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        issueArticleRepository.save(article.deleteArticle());
    }


    public UserEntity getUserEntity(String loginId) {
        return entityRetrievalService.getUserEntity(loginId);
    }
}
