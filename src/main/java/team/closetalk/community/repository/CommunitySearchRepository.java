package team.closetalk.community.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.community.entity.CommunityArticleEntity;

public interface CommunitySearchRepository extends JpaRepository<CommunityArticleEntity, Long> {

    Page<CommunityArticleEntity> findAllByTitleContainingOrContentContainingOrUserId_NicknameContaining(String TitleKeyword, String ContentKeyword,
                                                                                                 String NicknameKeyword, Pageable pageable);
    Page<CommunityArticleEntity> findAllByTitleContaining(String TitleKeyword, Pageable pageable);

    Page<CommunityArticleEntity> findAllByUserId_NicknameContaining(String NicknameKeyword, Pageable pageable);
}
