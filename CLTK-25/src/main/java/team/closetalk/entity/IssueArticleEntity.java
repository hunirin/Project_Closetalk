package team.closetalk.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "issue")
public class IssueArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long user_id;
    private String title;
    private String content;
    private String imageUrl;
    private Long hits;
    private LocalDateTime created_at;
    private LocalDateTime modified_at;
}
