package team.closetalk.ootd.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
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

    @ColumnDefault(value = "0")
    private Long likeCount;     // 좋아요 수

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "ootdArticle")
    private List<OotdArticleImagesEntity> ootdImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    // 좋아요 수 증가
    public void increaseLikeCount() {
        this.likeCount = likeCount + 1;
    }

    // 좋아요 수 감소
    public void decreaseLikeCount() {
        this.likeCount = likeCount - 1;
    }

}
