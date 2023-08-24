package team.closetalk.issue.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "issue_article_images")
public class IssueArticleImagesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "issueArticleId")
    private IssueArticleEntity issueArticle;
}
