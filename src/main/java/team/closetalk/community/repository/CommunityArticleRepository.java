package team.closetalk.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.community.entity.CommunityArticleEntity;

public interface CommunityArticleRepository extends JpaRepository<CommunityArticleEntity, Long> {
}
