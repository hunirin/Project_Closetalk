package team.closetalk.issue.entity;

import jakarta.persistence.*;
import lombok.Data;
import team.closetalk.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "issue")
public class IssueArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private Long userId;
    private String title;
    private String content;
    private String imageUrl;

    // 썸네일용
    private String thumbnail;

    @Column(nullable =  false)
    private Long hits;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "issueArticleEntityId")
    private List<IssueArticleImageEntity> issueImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity userId;
}
