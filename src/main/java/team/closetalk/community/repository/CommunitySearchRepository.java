package team.closetalk.community.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.closetalk.community.entity.CommunityArticleEntity;

public interface CommunitySearchRepository extends JpaRepository<CommunityArticleEntity, Long> {

    @Query("SELECT c FROM CommunityArticleEntity c WHERE c.title LIKE %:keyword% OR c.content LIKE %:keyword% OR c.userId.nickname LIKE %:keyword%")
    Page<CommunityArticleEntity> findAllByTitleOrContentOrUserId_NicknameContaining(@Param("keyword") String keyword, Pageable pageable);
    Page<CommunityArticleEntity> findAllByTitleContaining(String keyword, Pageable pageable);

    Page<CommunityArticleEntity> findAllByUserId_NicknameContaining(String keyword, Pageable pageable);
}
