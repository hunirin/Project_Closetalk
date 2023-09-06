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

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "ootd_article_id")
    private OotdArticleEntity ootdArticle;
}
