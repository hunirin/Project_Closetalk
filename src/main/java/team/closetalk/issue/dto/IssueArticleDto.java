package team.closetalk.issue.dto;

import lombok.Data;
import team.closetalk.issue.entity.IssueArticleEntity;
import team.closetalk.issue.entity.IssueArticleImageEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import java.util.List;

@Data
public class IssueArticleDto {
    private Long id;
    private String category;        // 카테고리
    private String title;           // 제목
    private String content;         // 내용
    private String thumbnail;       // 대표이미지
    private Long hits;              // 조회수
    private String createdAt;       // 작성 날짜
    private String nickname;        // 작성자

    // 이슈 전체 이미지 불러오기
    private List<IssueArticleImageDto> issueImages;

    // 게시글 상세 조회
    public static IssueArticleDto fromEntity(IssueArticleEntity entity) {
        IssueArticleDto dto = new IssueArticleDto();
        dto.setId(entity.getId());
        dto.setCategory(entity.getCategory());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setHits(entity.getHits());
        dto.setNickname(entity.getUserId().getNickname());

        if (entity.getModifiedAt() == null)
            dto.setCreatedAt(entity.getCreatedAt()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        else
            dto.setCreatedAt(entity.getCreatedAt()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " (수정됨");

        return dto;
    }
}