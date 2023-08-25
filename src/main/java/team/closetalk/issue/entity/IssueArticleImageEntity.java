package team.closetalk.issue.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "issue_article_images")
public class IssueArticleImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "issueArticle_id")
    private IssueArticleEntity issueArticle;
}
