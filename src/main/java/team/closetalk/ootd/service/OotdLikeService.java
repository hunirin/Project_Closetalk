package team.closetalk.ootd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.closet.service.EntityRetrievalService;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.entity.CommunityLikeEntity;
import team.closetalk.ootd.entity.OotdArticleEntity;
import team.closetalk.ootd.entity.OotdLikeEntity;
import team.closetalk.ootd.repository.OotdArticleRepository;
import team.closetalk.ootd.repository.OotdLikeRepository;
import team.closetalk.user.entity.UserEntity;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OotdLikeService {
    private final OotdLikeRepository ootdLikeRepository;
    private final OotdArticleRepository ootdArticleRepository;
    private final EntityRetrievalService entityRetrievalService;

    public String likeOotdArticle(Long articleId, Authentication authentication) {
        UserEntity user = getUserEntity(authentication.getName());

        OotdArticleEntity article = ootdArticleRepository.findById(articleId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!article.getUserEntity().equals(user)) {
            Optional<OotdLikeEntity> checkLike = ootdLikeRepository.findByUserIdAndOotdArticleId(user, article);
            // 좋아요 누른 적이 없으면 좋아요
            if (checkLike.isEmpty()) {
                OotdLikeEntity like = new OotdLikeEntity();
                like.setUserId(user);
                like.setOotdArticleId(article);
                ootdLikeRepository.save(like);

                article.increaseLikeCount();
                ootdArticleRepository.save(article);
                return "좋아요";
            } else {
                // 좋아요를 누른 적이 있다면 좋아요 취소
                ootdLikeRepository.delete(checkLike.get());
                ootdLikeRepository.flush();

                article.decreaseLikeCount();
                ootdArticleRepository.save(article);
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


