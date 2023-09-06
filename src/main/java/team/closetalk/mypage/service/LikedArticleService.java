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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikedArticleService {
    private final CommunityArticleRepository communityArticleRepository;
    private final LikedArticleRepository likedArticleRepository;
    private final CommunityLikeRepository communityLikeRepository;

    @Transactional(readOnly = true)
    public Page<CommunityArticleListDto> readLikedArticlePaged(Integer page, Integer limit, Authentication authentication) {
        Pageable pageable = PageRequest.of(
                page, limit, Sort.by("id").ascending());

        List<Long> likedArticleIds =
                likedArticleRepository.findLikedArticleIdsByLoginId(authentication.getName());

        List<CommunityArticleEntity> communityArticleEntities =
                communityArticleRepository.findAllByIdIn(likedArticleIds);

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
}