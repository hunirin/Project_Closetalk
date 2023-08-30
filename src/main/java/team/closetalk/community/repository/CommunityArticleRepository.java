package team.closetalk.community.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.community.entity.CommunityArticleEntity;

import java.util.Optional;

public interface CommunityArticleRepository extends JpaRepository<CommunityArticleEntity, Long> {
    Optional<CommunityArticleEntity> findByIdAndUserId(Long articleId, Long userId);

    Page<CommunityArticleEntity> findByCategory(String category, Pageable pageable);
}
