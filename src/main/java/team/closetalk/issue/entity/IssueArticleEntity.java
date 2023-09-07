package team.closetalk.issue.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "issue_article")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssueArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;        // 카테고리
    @Column(nullable = false)
    private String title;           // 제목
    @Column(nullable = false)
    private String content;         // 내용
    private String thumbnail;       // 대표이미지
    @ColumnDefault(value = "0")
    private Long hits;              // 조회수

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "issueArticleId")
    private List<IssueArticleImageEntity> issueImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    public IssueArticleEntity(String category, String title,
                              String content, UserEntity user) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.userId = user;
        this.hits = 0L;
    }

    // 조회수 증가
    public IssueArticleEntity increaseHit() {
        this.hits = hits + 1;
        return this;
    }

    public IssueArticleEntity deleteArticle() {
        this.deletedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        return this;
    }

    // 썸네일 저장
    public IssueArticleEntity saveThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }
}
