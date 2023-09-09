package team.closetalk.issue.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "issue_article_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssueArticleImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issueArticle_id")
    private IssueArticleEntity issueArticleId;

    public IssueArticleImageEntity(IssueArticleEntity article, String imageFilePath) {
        this.issueArticleId = article;
        this.imageUrl = imageFilePath;
    }
}
