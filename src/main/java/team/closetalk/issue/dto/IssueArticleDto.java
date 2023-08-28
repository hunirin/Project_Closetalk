package team.closetalk.issue.dto;

import lombok.Data;
import team.closetalk.issue.entity.IssueArticleEntity;
import team.closetalk.issue.entity.IssueArticleImageEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import java.util.List;

@Data
public class IssueArticleDto {
    private Long id;
    private String title;
    private String content;
    private String thumbnail;
    private Long hits;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // 이슈 전체 이미지 불러오기
    private List<IssueArticleImageDto> issueImages;

    public static IssueArticleDto fromEntity(IssueArticleEntity entity) {
        IssueArticleDto dto = new IssueArticleDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setHits(entity.getHits());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setModifiedAt(entity.getModifiedAt());

        return dto;
    }
}