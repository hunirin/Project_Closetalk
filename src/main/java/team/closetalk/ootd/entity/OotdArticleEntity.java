package team.closetalk.ootd.entity;

import jakarta.persistence.*;
import lombok.Data;
import team.closetalk.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "ootd_article")
public class OotdArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String hashtag;

    // 대표 이미지
    private String thumbnail;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "ootdArticle")
    private List<OotdArticleImagesEntity> ootdImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

}
