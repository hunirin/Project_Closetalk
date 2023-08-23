package team.closetalk.community.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "communityArticle")
public class CommunityArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;    // 카테고리
    private String title;       // 제목
    private String content;     // 내용
    private Long hits;          // 조회수
    private String thumbnail;   // 대표이미지
//    private LocalDateTime createdAt;    // 작성 날짜
//    private LocalDateTime modifiedAt;    // 수정 날짜

    @OneToMany(mappedBy = "communityArticle")
    private List<CommunityArticleImagesEntity> communityImages = new ArrayList<>();

    @OneToMany(mappedBy = "communityArticle")
    private List<CommunityCommentEntity> communityComments = new ArrayList<>();
}
