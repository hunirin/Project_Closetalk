package team.closetalk.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.community.dto.CommunityArticleDto;
import team.closetalk.community.dto.CommunityArticleImagesDto;
import team.closetalk.community.dto.CommunityArticleListDto;
import team.closetalk.community.dto.CommunityCommentDto;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.entity.CommunityArticleImagesEntity;
import team.closetalk.community.enumeration.CommunityCategoryEnum;
import team.closetalk.community.repository.CommunityArticleImagesRepository;
import team.closetalk.community.repository.CommunityArticleRepository;
import team.closetalk.user.entity.UserEntity;
import team.closetalk.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommunityArticleService {
    private final CommunityArticleRepository communityArticleRepository;
    private final CommunityArticleImagesRepository communityArticleImagesRepository;
    private final CommunityCommentService communityCommentService;
    private final UserRepository userRepository;

    // 커뮤니티 전체 게시물 조회(페이지 단위로 조회)
    public Page<CommunityArticleListDto> readCommunityPaged(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").ascending());

        Page<CommunityArticleEntity> communityEntityPage =
                communityArticleRepository.findAllByDeletedAtIsNull(pageable);
        return communityEntityPage.map(CommunityArticleListDto::fromEntity);
    }

    // 커뮤니티 게시물 전체 검색
    public Page<CommunityArticleListDto> searchCommunityPaged(String titleKeyword, String contentKeyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").ascending());

        Page<CommunityArticleEntity> communityEntityPage =
                communityArticleRepository.findAllByTitleContainingOrContentContaining(titleKeyword, contentKeyword, pageable);

        return communityEntityPage.map(CommunityArticleListDto::fromEntity);
    }

    // 커뮤니티 게시물 제목 검색
//    public Page<CommunityArticleListDto> searchTitleCommunityPaged(String titleKeyword, Integer pageNum, Integer pageSize) {
//        Pageable pageable = PageRequest.of(
//                pageNum, pageSize, Sort.by("id").ascending());
//
//        Page<CommunityArticleEntity> communityEntityPage =
//                communityArticleRepository.findAllByTitleContaining(titleKeyword, pageable);
//        return communityEntityPage.map(CommunityArticleListDto::fromEntity);
//    }

    // 커뮤니티 게시물 닉네임 검색
//    public Page<CommunityArticleListDto> searchNicknameCommunityPaged(String nicknameKeyword, Integer pageNum, Integer pageSize) {
//        Pageable pageable = PageRequest.of(
//                pageNum, pageSize, Sort.by("id").ascending());
//
//        Page<CommunityArticleEntity> communityEntityPage =
//                communityArticleRepository.findAllByNicknameContaining(nicknameKeyword, pageable);
//        return communityEntityPage.map(CommunityArticleListDto::fromEntity);
//    }

    // 카테고리별 게시물 조회(페이지 단위로 조회)
    public Page<CommunityArticleListDto> readCommunityByCategory(CommunityCategoryEnum category,
                                                                 Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").ascending());

        Page<CommunityArticleEntity> communityEntityPage =
                communityArticleRepository.findAllByCategoryAndDeletedAtIsNull(category, pageable);
        return communityEntityPage.map(CommunityArticleListDto::fromEntity);
    }

    // 상세 페이지 조회
    public CommunityArticleDto readArticleOne(Long articleId) {
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
                communityArticleImagesRepository.findAllByCommunityArticle_Id(article.getId());
        List<CommunityArticleImagesDto> imagesDtoList =
                imagesEntityList.stream().map(CommunityArticleImagesDto::fromEntity).toList();

        return CommunityArticleDto.detailFromEntity(article, commentDtoList, imagesDtoList);
    }

    // 게시글 수정
    public CommunityArticleDto updateCommunityArticle(Long articleId,
                                                      Authentication authentication,
                                                      CommunityArticleDto dto) {
        UserEntity user = userRepository.findByLoginId(authentication.getName())
                .orElseThrow(() -> {
                    log.error("수정에 실패하였습니다.");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });

        CommunityArticleEntity article = communityArticleRepository.findByIdAndUserId_Id(articleId, user.getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setModifiedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        communityArticleRepository.save(article);

        return readArticleOne(articleId);
    }

    // 게시글 삭제
    public void deleteArticle(Long articleId, Authentication authentication) {
        UserEntity user = userRepository.findByLoginId(authentication.getName())
                .orElseThrow(() -> {
                    log.error("삭제에 실패하였습니다.");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
        CommunityArticleEntity communityArticle = communityArticleRepository.findByIdAndUserId_Id(articleId, user.getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!Objects.equals(communityArticle.getUserId().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        communityArticleRepository.save(communityArticle.deleteArticle());
    }
}
