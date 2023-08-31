package team.closetalk.community.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.enumeration.CommunityCategoryEnum;

import java.util.Optional;

public interface CommunityArticleRepository extends JpaRepository<CommunityArticleEntity, Long> {
    Optional<CommunityArticleEntity> findByIdAndUserId_Id(Long articleId, Long userId);
    Page<CommunityArticleEntity> findAll(Specification<CommunityArticleEntity> spec, Pageable pageable);
    Page<CommunityArticleEntity> findByCategory(CommunityCategoryEnum category, Pageable pageable);
}
