package team.closetalk.community.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.closetalk.community.entity.CommunityArticleEntity;

public interface CommunitySearchRepository extends JpaRepository<CommunityArticleEntity, Long> {

    Page<CommunityArticleEntity> findAllByTitleContainingOrContentContainingOrNicknameContaining(String TitleKeyword, String ContentKeyword,
                                                                                                 String NicknameKeyword, Pageable pageable);
    Page<CommunityArticleEntity> findAllByTitleContaining(String TitleKeyword, Pageable pageable);

    Page<CommunityArticleEntity> findAllByNicknameContaining(String NicknameKeyword, Pageable pageable);
}
