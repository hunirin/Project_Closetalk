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
    @Column(name = "deleted_at")
    private LocalDate deletedAt;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity userId;

    @ManyToOne
    @JoinColumn(name = "communityArticle")
    private CommunityArticleEntity communityArticle;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private CommunityCommentEntity commentId;

    // 댓글 생성
    public CommunityCommentEntity(String content, UserEntity user,
                                  CommunityArticleEntity article,
                                  CommunityCommentEntity comment) {
        this.content = content;
        this.userId = user;
        this.communityArticle = article;
        this.commentId = comment;
        this.createdAt = LocalDate.now(ZoneId.of("Asia/Seoul"));
    }

    // 댓글 삭제(Soft Delete)
    public CommunityCommentEntity deleteEntity() {
        this.deletedAt = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return this;
    }

    // 댓글 수정
    public CommunityCommentEntity updateEntity(String content) {
        this.content = content;
        this.modifiedAt = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return this;
    }
}
