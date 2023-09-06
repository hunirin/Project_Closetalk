package team.closetalk.community.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.closetalk.user.entity.UserEntity;

@Entity
@Data
@Table(name = "community_like")
@NoArgsConstructor
public class CommunityLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_article_id")
    private CommunityArticleEntity communityArticleId;
}
