package team.closetalk.community.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.enumeration.Category;

import java.util.List;
import java.util.Optional;

public interface CommunityArticleRepository extends JpaRepository<CommunityArticleEntity, Long> {
    Optional<CommunityArticleEntity> findByIdAndUserId_Id(Long articleId, Long userId);
    Page<CommunityArticleEntity> findAllByDeletedAtIsNull(Pageable pageable);
    Page<CommunityArticleEntity> findAllByCategoryAndDeletedAtIsNull(Category category, Pageable pageable);

    List<CommunityArticleEntity> findAllByIdIn(List<Long> articleIds);
}
