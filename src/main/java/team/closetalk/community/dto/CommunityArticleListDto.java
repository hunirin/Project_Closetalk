package team.closetalk.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.enumeration.Category;

import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CommunityArticleListDto {
    private Long id;
    private Category category;    // 카테고리
    private String title;       // 제목
    private Long hits;          // 조회수
    private Long likeCount;     // 좋아요 수
    private String thumbnail;   // 대표이미지
    private String createdAt;    // 작성 날짜

    // 게시글 목록 조회
    public static CommunityArticleListDto fromEntity(CommunityArticleEntity entity) {

        return CommunityArticleListDto.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .title(entity.getTitle())
                .hits(entity.getHits())
                .likeCount(entity.getLikeCount())
                .thumbnail(entity.getThumbnail())
                .createdAt(entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))) // 임시
                .build();
    }

    // 좋아요 수 조회
    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }
}