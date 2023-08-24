package team.closetalk.issue.entity;

import jakarta.persistence.*;
import lombok.Data;
import team.closetalk.user.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "issueArticle")
public class IssueArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private Long hit;

    // 대표 이미지
    private String thumbnail;

    @OneToMany(mappedBy = "issueArticle")
    private List<IssueArticleImagesEntity> issueImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;
}
