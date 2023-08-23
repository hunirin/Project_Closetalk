package team.closetalk.community.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "communityArticleImages")
public class CommunityArticleImagesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "communityArticle_id")
    private CommunityArticleEntity communityArticle;
}
