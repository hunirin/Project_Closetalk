package team.closetalk.community.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "communityComment")
public class CommunityCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;             // 내용

    private LocalDate createdAt;    // 작성 날짜
    private LocalDate modifiedAt;   // 수정 날짜

    @ManyToOne
    @JoinColumn(name = "communityArticle_id")
    private CommunityArticleEntity communityArticle;

    @ManyToOne
    @JoinColumn(name = "parentComment_id")  // 이름 변경
    private CommunityCommentEntity parentComment;  // 자기 자신과의 관계

    @OneToMany(mappedBy = "parentComment")  // mappedBy 수정
    private List<CommunityCommentEntity> childComments = new ArrayList<>();
}
