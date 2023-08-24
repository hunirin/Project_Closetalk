package team.closetalk.issue.entity;

import jakarta.persistence.*;
import lombok.Data;

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
}
