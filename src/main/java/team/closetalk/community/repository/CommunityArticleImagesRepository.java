package team.closetalk.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.community.entity.CommunityArticleImagesEntity;

import java.util.List;

public interface CommunityArticleImagesRepository extends JpaRepository<CommunityArticleImagesEntity, Long> {
    List<CommunityArticleImagesEntity> findAllByCommunityArticleId_Id(Long articleId);
}
