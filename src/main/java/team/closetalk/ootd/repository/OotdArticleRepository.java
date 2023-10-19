package team.closetalk.ootd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.ootd.entity.OotdArticleEntity;

import java.util.Optional;

public interface OotdArticleRepository extends JpaRepository<OotdArticleEntity, Long> {
    Page<OotdArticleEntity> findByIsDeletedFalse(Pageable pageable);

    Optional<OotdArticleEntity> findByIdAndIsDeletedFalse(Long articleId);
}
