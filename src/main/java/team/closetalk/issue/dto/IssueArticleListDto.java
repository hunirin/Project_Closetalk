package team.closetalk.issue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import team.closetalk.community.dto.article.response.CommunityArticleListDto;
import team.closetalk.issue.entity.IssueArticleEntity;

import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class IssueArticleListDto {
    private Long id;
    private String category;    // 카테고리
    private String title;       // 제목
    private Long hits;          // 조회수
    private String thumbnail;   // 대표이미지
    private String createdAt;   // 작성 날짜
    private String nickname;    // 작성자

    // 게시글 전체 조회
    public static CommunityArticleListDto fromEntity(IssueArticleEntity entity) {

        return CommunityArticleListDto.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .title(entity.getTitle())
                .hits(entity.getHits())
                .thumbnail(entity.getThumbnail())
                .createdAt(entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .nickname(entity.getUserId().getNickname())
                .build();
    }
}
