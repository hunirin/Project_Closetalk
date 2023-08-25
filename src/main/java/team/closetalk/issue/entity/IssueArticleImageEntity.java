package team.closetalk.issue.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "issueArticle_images")
public class IssueArticleImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private IssueArticleEntity issueArticleEntityId;
}
