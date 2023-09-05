package team.closetalk.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.entity.CommunityLikeEntity;
import team.closetalk.user.entity.UserEntity;

import java.util.Optional;


public interface CommunityLikeRepository extends JpaRepository<CommunityLikeEntity, Long> {

//    Optional<CommunityLikeEntity> findByUserIdAndArticleId(Long userId, Long articleId);
    Optional<CommunityLikeEntity> findByUserIdIdAndArticleIdId(Long userId, Long articleId);
}
