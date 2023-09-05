package team.closetalk.community.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import team.closetalk.community.entity.CommunityLikeEntity;

import java.util.Optional;

public interface CommunityLikeRepository extends JpaRepository<CommunityLikeEntity, Long> {

    Optional<CommunityLikeEntity> findByUserIdAndArticleId(Long userId, Long articleId);

}
