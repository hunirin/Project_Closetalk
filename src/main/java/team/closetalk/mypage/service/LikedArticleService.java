package team.closetalk.mypage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.closetalk.community.dto.article.response.CommunityArticleListDto;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.repository.CommunityArticleRepository;
import team.closetalk.community.repository.CommunityLikeRepository;
import team.closetalk.mypage.repository.LikedArticleRepository;
import team.closetalk.ootd.dto.OotdArticleDto;
import team.closetalk.ootd.entity.OotdArticleEntity;
import team.closetalk.ootd.repository.OotdArticleRepository;
import team.closetalk.ootd.repository.OotdLikeRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikedArticleService {
    private final CommunityArticleRepository communityArticleRepository;
    private final LikedArticleRepository likedArticleRepository;
    private final CommunityLikeRepository communityLikeRepository;
    private final OotdArticleRepository ootdArticleRepository;
    private final OotdLikeRepository ootdLikeRepository;

    // Community
    // 좋아요한 글 목록보기
    @Transactional(readOnly = true)
    public Page<CommunityArticleListDto> readLikedCommunityArticlePaged(Integer page, Integer limit, Authentication authentication) {
        Pageable pageable = PageRequest.of(
                page, limit, Sort.by("id").descending()); // 최신순을 하기위해 역순

        List<Long> likedArticleIds =
                likedArticleRepository.findLikedCommunityArticleIdsByLoginId(authentication.getName());

        List<CommunityArticleEntity> communityArticleEntities =
                communityArticleRepository.findAllByIdIn(likedArticleIds);
        log.info(likedArticleIds.toString());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), communityArticleEntities.size());
        List<CommunityArticleEntity> paginatedLikedArticles = communityArticleEntities.subList(start, end);

        Page<CommunityArticleEntity> likedArticleListPage =
                new PageImpl<>(paginatedLikedArticles, pageable, communityArticleEntities.size());
        Page<CommunityArticleListDto> articleListDtoPage =
                likedArticleListPage.map(CommunityArticleListDto::fromEntity);

        // 각 게시물의 좋아요 수 조회
        articleListDtoPage.forEach(dto -> {
            Long likeCount = communityLikeRepository.countByCommunityArticleId_Id(dto.getId());
            dto.setLikeCount(likeCount);
        });

        return articleListDtoPage;
    }

    // OOTD
    // 좋아요한 글 목록보기
    @Transactional(readOnly = true)
    public Page<OotdArticleDto> readLikedOotdArticlePaged(Integer page, Integer limit, Authentication authentication) {
        Pageable pageable = PageRequest.of(
                page, limit, Sort.by("id").descending()); // 최신순으로 하기위해 역순

        List<Long> likedArticleIds =
                likedArticleRepository.findLikedOotdArticleIdsByLoginId(authentication.getName());
        log.info(authentication.getName());

        List<OotdArticleEntity> ootdArticleEntities =
                ootdArticleRepository.findAllById(likedArticleIds);
        log.info(likedArticleIds.toString());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), ootdArticleEntities.size());
        List<OotdArticleEntity> paginatedLikedArticles = ootdArticleEntities.subList(start, end);

        Page<OotdArticleEntity> likedArticleListPage =
                new PageImpl<>(paginatedLikedArticles, pageable, ootdArticleEntities.size());
        Page<OotdArticleDto> articleListDtoPage =
                likedArticleListPage.map(OotdArticleDto::fromEntityForList);

        articleListDtoPage.forEach(dto -> {
            Long likeCount = ootdLikeRepository.countByOotdArticleId_Id(dto.getId());
            dto.setLikeCount(likeCount);
        });

        return articleListDtoPage;
    }
}
