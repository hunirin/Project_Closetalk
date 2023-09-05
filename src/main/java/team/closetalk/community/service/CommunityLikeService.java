package team.closetalk.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.closet.service.EntityRetrievalService;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.entity.CommunityLikeEntity;
import team.closetalk.community.repository.CommunityArticleRepository;
import team.closetalk.community.repository.CommunityLikeRepository;
import team.closetalk.user.entity.UserEntity;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityLikeService {
    private final CommunityLikeRepository communityLikeRepository;
    private final CommunityArticleRepository communityArticleRepository;
    private final EntityRetrievalService entityRetrievalService;

    public String likeCommunityArticle(Long articleId, Authentication authentication) {
        UserEntity user = getUserEntity(authentication.getName());

        CommunityArticleEntity article = communityArticleRepository.findById(articleId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!article.getUserId().equals(user)) {
            Optional<CommunityLikeEntity> checkLike = communityLikeRepository.findByUserIdIdAndArticleIdId(user.getId(), articleId);
            // 좋아요 누른 적이 없으면 좋아요
            if (checkLike.isEmpty()) {
                CommunityLikeEntity like = new CommunityLikeEntity();
                like.setUserId(user);
                like.setArticleId(article);
                communityLikeRepository.save(like);
                return "좋아요";
            } else {
                // 좋아요를 누른 적이 있다면 좋아요 취소
                communityLikeRepository.delete(checkLike.get());
                communityLikeRepository.flush();
                return "좋아요 취소";
            }
        } else {
            // 본인이 쓴 게시글이 아닌 경우
            return "본인이 작성한 게시글에는 좋아요가 불가능합니다.";
        }
    }

    public UserEntity getUserEntity(String loginId) {
        return entityRetrievalService.getUserEntity(loginId);
    }
}
