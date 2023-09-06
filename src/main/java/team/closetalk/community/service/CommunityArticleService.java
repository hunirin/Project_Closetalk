package team.closetalk.community.service;

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
import team.closetalk.closet.dto.ClosetItemDto;
import team.closetalk.closet.repository.ClosetItemRepository;
import team.closetalk.closet.service.EntityRetrievalService;
import team.closetalk.community.dto.*;
import team.closetalk.community.dto.article.response.CommunityArticleDto;
import team.closetalk.community.dto.article.response.CommunityArticleImagesDto;
import team.closetalk.community.dto.article.response.CommunityArticleListDto;
import team.closetalk.community.dto.article.request.CommunityCreateArticleDto;
import team.closetalk.community.entity.composite.CommunityArticleClosetItems;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.entity.CommunityArticleImagesEntity;
import team.closetalk.community.enumeration.Category;
import team.closetalk.community.repository.ArticleAndClosetItemRepository;
import team.closetalk.community.repository.CommunityArticleImagesRepository;
import team.closetalk.community.repository.CommunityArticleRepository;
import team.closetalk.community.repository.CommunityLikeRepository;
import team.closetalk.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommunityArticleService {
    private final CommunityArticleRepository communityArticleRepository;
    private final CommunityArticleImagesRepository communityArticleImagesRepository;
    private final ClosetItemRepository closetItemRepository;
    private final ArticleAndClosetItemRepository articleAndClosetItemRepository;
    private final EntityRetrievalService entityRetrievalService;
    private final CommunityCommentService communityCommentService;
    private final CommunityArticleSaveImageService saveImageService;
    private final CommunityLikeRepository communityLikeRepository;

    // 커뮤니티 전체 게시물 조회(페이지 단위로 조회)
    public Page<CommunityArticleListDto> readCommunityPaged(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").ascending());

        Page<CommunityArticleEntity> communityEntityPage =
                communityArticleRepository.findAllByDeletedAtIsNull(pageable);

        Page<CommunityArticleListDto> articleListDtoPage = communityEntityPage.map(CommunityArticleListDto::fromEntity);

        // 각 게시물의 좋아요 수 조회
        articleListDtoPage.forEach(dto -> {
            Long likeCount = countLike(dto.getId());
            dto.setLikeCount(likeCount);
        });

        return articleListDtoPage;
    }

    // 카테고리별 게시물 조회(페이지 단위로 조회)
    public Page<CommunityArticleListDto> readCommunityPagedByCategory(Category category,
                                                                      Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").ascending());

        Page<CommunityArticleEntity> communityEntityPage =
                communityArticleRepository.findAllByCategoryAndDeletedAtIsNull(category, pageable);
        return communityEntityPage.map(CommunityArticleListDto::fromEntity);
    }

    // 상세 페이지 조회
    public CommunityArticleDto readArticle(Long articleId) {
        CommunityArticleEntity article = communityArticleRepository.findById(articleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (article.getDeletedAt() != null) {
            log.error("삭제된 게시물입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        communityArticleRepository.save(article.increaseHit()); // 조회수 증가
        List<CommunityCommentDto> commentDtoList =
                communityCommentService.readCommentList(article.getId());
        List<CommunityArticleImagesEntity> imagesEntityList =
                communityArticleImagesRepository.findAllByCommunityArticleId_Id(article.getId());
        List<CommunityArticleImagesDto> imagesDtoList =
                imagesEntityList.stream().map(CommunityArticleImagesDto::fromEntity).toList();
        List<CommunityArticleClosetItems> closetItemIdList =
                articleAndClosetItemRepository.findAllByCommunityArticleId(article);
        List<ClosetItemDto> closetItemDtoList = new ArrayList<>();
        for (CommunityArticleClosetItems closetItemId : closetItemIdList) {
            Long closetItemNumber = closetItemId.getId().getClosetItemId();
            ClosetItemDto closetItemDto =
                    ClosetItemDto.toClosetItemDto(closetItemRepository.findById(closetItemNumber)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
            closetItemDtoList.add(closetItemDto);
        }

        return CommunityArticleDto.detailFromEntity(article, commentDtoList, imagesDtoList, closetItemDtoList);
    }

    // 게시글 수정
    public CommunityArticleDto updateArticle(Long articleId,
                                             Authentication authentication,
                                             CommunityArticleDto dto) {
        UserEntity user = getUserEntity(authentication.getName());

        CommunityArticleEntity article = communityArticleRepository.findByIdAndUserId_Id(articleId, user.getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setModifiedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        communityArticleRepository.save(article);

        return readArticle(articleId);
    }

    // 게시글 삭제
    public void deleteArticle(Long articleId, Authentication authentication) {
        UserEntity user = getUserEntity(authentication.getName());
        CommunityArticleEntity article = communityArticleRepository.findByIdAndUserId_Id(articleId, user.getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!Objects.equals(article.getUserId().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        communityArticleRepository.save(article.deleteArticle());
    }

    // 게시물 생성(이미지 포함)
    public CommunityArticleDto createArticle(CommunityCreateArticleDto dto,
                                             List<MultipartFile> imageUrlList,
                                             Authentication authentication) {
        UserEntity user = getUserEntity(authentication.getName());
        CommunityArticleEntity article =
                new CommunityArticleEntity(dto.getCategory(), dto.getTitle(), dto.getContent(), user);
        communityArticleRepository.save(article);
        if (dto.getSelectClosetItemNumList() != null) saveImageService.saveArticleWithCloset(dto, article);
        if (imageUrlList != null) {
            saveImageService.saveArticleImage(article, imageUrlList);
            CommunityArticleImagesEntity imagesEntityList =
                    communityArticleImagesRepository.findAllByCommunityArticleId_Id(article.getId()).get(0);
            communityArticleRepository.save(article.saveThumbnail(imagesEntityList.getImageUrl()));
        }
        return readArticle(article.getId());
    }

    // LoginId == authentication.getName() -> UserEntity 찾기
    public UserEntity getUserEntity(String loginId) {
        return entityRetrievalService.getUserEntity(loginId);
    }

    // 게시글 좋아요 수 체크
    private Long countLike(Long articleId) {
        return communityLikeRepository.countByCommunityArticleId_Id(articleId);
    }
}
