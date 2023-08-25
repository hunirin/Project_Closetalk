package team.closetalk.issue.entity;

import jakarta.persistence.*;
import lombok.Data;
import team.closetalk.user.entity.UserEntity;

<<<<<<< HEAD
import java.time.LocalDateTime;
=======
>>>>>>> origin/CLTK-35
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
<<<<<<< HEAD
@Table(name = "issue")
=======
@Table(name = "issue_article")
>>>>>>> origin/CLTK-35
public class IssueArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
<<<<<<< HEAD
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
=======

    private String title;
    private String content;
    private Long hit;

    // 대표 이미지
    private String thumbnail;

    @OneToMany(mappedBy = "issueArticle")
    private List<IssueArticleImagesEntity> issueImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
>>>>>>> origin/CLTK-35
    private UserEntity userId;
}
