package team.closetalk.ootd.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ootd_article_images")
public class OotdArticleImagesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "ootdArticle_id")
    private OotdArticleEntity ootdArticle;
}
