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
    Page<CommunityArticleEntity> findAllByDeletedAtIsNull(Pageable pageable);
    Page<CommunityArticleEntity> findAllByCategoryAndDeletedAtIsNull(CommunityCategoryEnum category, Pageable pageable);

    Page<CommunityArticleEntity> findAllByTitleContainingOrContentContaining(String TitleKeyword, String ContentKeyword, Pageable pageable);
    Page<CommunityArticleEntity> findAllByTitleContaining(String TitleKeyword, Pageable pageable);
//    Page<CommunityArticleEntity> findAllByNicknameContaining(String NicknameKeyword, Pageable pageable);
}
