package team.closetalk.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.community.entity.CommunityLikeEntity;

import java.util.Optional;


public interface CommunityLikeRepository extends JpaRepository<CommunityLikeEntity, Long> {
    Optional<CommunityLikeEntity> findByUserIdIdAndArticleIdId(Long userId, Long articleId);

    Long countByArticleIdId(Long articleId);
}
