package team.closetalk.ootd.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ootdArticleImages")
public class OotdArticleImagesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "ootdArticle_id")
    private OotdArticleEntity ootdArticle;
}
