package team.closetalk.ootd.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.closetalk.user.entity.UserEntity;

@Data
@Entity
@Table(name = "ootd_like")
@NoArgsConstructor
public class OotdLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ootd_article_id")
    private OotdArticleEntity ootdArticleId;
}
