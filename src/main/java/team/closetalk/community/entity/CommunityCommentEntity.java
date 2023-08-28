package team.closetalk.community.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.closetalk.user.entity.UserEntity;

import java.time.LocalDate;
import java.time.ZoneId;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "communityComment")
public class CommunityCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String content;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "modified_at")
    private LocalDate modifiedAt;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity userId;

    @ManyToOne
    @JoinColumn(name = "communityArticle")
    private CommunityArticleEntity communityArticle;

//    @ManyToOne
//    @JoinColumn(name = "parent_comment_id")
//    private CommunityCommentEntity parentComment;

//    @OneToMany(mappedBy = "parentComment")  // mappedBy 수정
//    private List<CommunityCommentEntity> childComments = new ArrayList<>();

    // 댓글 생성
    public CommunityCommentEntity(String content, UserEntity user,
                                  CommunityArticleEntity article) {
        this.content = content;
        this.userId = user;
        this.communityArticle = article;
        this.createdAt = LocalDate.now(ZoneId.of("Asia/Seoul"));
    }
}
