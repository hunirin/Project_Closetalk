package team.closetalk.community.entity.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ArticleClosetItemId implements Serializable {
    @Column(name = "community_article_id")
    private Long communityArticleId;

    @Column(name = "closet_item_id")
    private Long closetItemId;
}