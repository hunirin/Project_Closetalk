package team.closetalk.ootd.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "ootdArticle")
public class OotdArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String hashtag;

    // 대표 이미지
    private String thumbnail;

    @OneToMany(mappedBy = "ootdArticle")
    private List<OotdArticleImagesEntity> ootdImages = new ArrayList<>();

}
