package team.closetalk.issue.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import team.closetalk.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "issue_article")
public class IssueArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    // 미리보기용
    private String thumbnail;

    @ColumnDefault(value = "0")
    private Long hits;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "issueArticle")
    private List<IssueArticleImageEntity> issueImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "users")
    private UserEntity user;
}
