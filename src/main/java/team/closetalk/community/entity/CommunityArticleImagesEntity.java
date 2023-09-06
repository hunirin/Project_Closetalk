package team.closetalk.community.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "community_article_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityArticleImagesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_article_id")
    private CommunityArticleEntity communityArticleId;

    public CommunityArticleImagesEntity(CommunityArticleEntity article,
                                        String imageFilePath) {
        this.communityArticleId = article;
        this.imageUrl = imageFilePath;
    }
}
