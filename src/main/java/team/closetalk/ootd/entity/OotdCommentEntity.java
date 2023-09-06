package team.closetalk.ootd.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.closetalk.user.entity.UserEntity;

import java.time.LocalDate;
import java.time.ZoneId;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ootdComment")
public class OotdCommentEntity {
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
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "ootdArticle_id")
    private OotdArticleEntity ootdArticle;

    @ManyToOne
    @JoinColumn(name = "ootdComment_id")
    private OotdCommentEntity commentId;

    // 댓글 생성
    public OotdCommentEntity(String content, UserEntity user,
                                  OotdArticleEntity article,
                                  OotdCommentEntity comment) {
        this.content = content;
        this.userEntity = user;
        this.ootdArticle = article;
        this.commentId = comment;
        this.createdAt = LocalDate.now(ZoneId.of("Asia/Seoul"));
    }

    // 댓글 삭제(Soft Delete)
    public OotdCommentEntity deleteEntity() {
        this.deletedAt = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return this;
    }

    // 댓글 수정
    public OotdCommentEntity updateEntity(String content) {
        this.content = content;
        this.modifiedAt = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return this;
    }
}
