package team.closetalk.issue.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.closetalk.issue.entity.IssueArticleEntity;


public interface IssueArticleSearchRepository extends JpaRepository<IssueArticleEntity, Long> {

    @Query("SELECT c FROM IssueArticleEntity  c WHERE c.title LIKE %:keyword% OR c.content LIKE %:keyword% OR c.userId.nickname LIKE %:keyword% OR c.userId.nickname LIKE %:keyword%")
    Page<IssueArticleEntity> findAllByTitleOrContentOrUserId_NicknameContaining(@Param("keyword") String keyword, Pageable pageable);

    Page<IssueArticleEntity> findAllByTitleContaining(String keyword, Pageable pageable);

    Page<IssueArticleEntity> findAllByUserId_NicknameContaining(String keyword, Pageable pageable);
}
