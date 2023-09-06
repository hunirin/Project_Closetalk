package team.closetalk.issue.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.issue.entity.IssueArticleEntity;

import java.util.Optional;

public interface IssueArticleRepository extends JpaRepository<IssueArticleEntity, Long> {
    Page<IssueArticleEntity> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<IssueArticleEntity> findByIdAndUserId_Id(Long articleId, Long userId);
}
