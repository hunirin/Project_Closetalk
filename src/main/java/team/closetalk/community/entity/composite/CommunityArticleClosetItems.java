package team.closetalk.community.entity.composite;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.closetalk.closet.entity.ClosetItemEntity;
import team.closetalk.community.entity.CommunityArticleEntity;

@Data
@Entity
@Table(name = "closetalk_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityArticleClosetItems {
    @EmbeddedId
    private ArticleClosetItemId id;

    @MapsId("communityArticleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_article_id")
    private CommunityArticleEntity communityArticleId;

    @MapsId("closetItemId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closet_item_id")
    private ClosetItemEntity closetItemId;

    public CommunityArticleClosetItems(ArticleClosetItemId articleClosetItemId,
                                       CommunityArticleEntity article,
                                       ClosetItemEntity closetItemId) {
        this.id = articleClosetItemId;
        this.communityArticleId = article;
        this.closetItemId = closetItemId;
    }
}