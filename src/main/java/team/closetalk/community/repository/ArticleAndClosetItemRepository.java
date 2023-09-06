package team.closetalk.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.community.entity.composite.CommunityArticleClosetItems;
import team.closetalk.community.entity.CommunityArticleEntity;

import java.util.List;

public interface ArticleAndClosetItemRepository
        extends JpaRepository<CommunityArticleClosetItems, CommunityArticleEntity> {
    List<CommunityArticleClosetItems> findAllByCommunityArticleId(CommunityArticleEntity article);
}