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

    private static LocalDateTime presentLocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static IssueArticleDto fromEntity(IssueArticleEntity entity) {
        IssueArticleDto dto = new IssueArticleDto();
        dto.setId(entity.getId());
//        dto.setUserId(entity.getUserId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
//        dto.setImageUrl(entity.getImageUrl());
        dto.setHits(entity.getHits());
//        dto.setCreatedAt(entity.getCreatedAt());
//        dto.setModifiedAt(entity.getModifiedAt());

        List<IssueArticleImageEntity> issueImages = entity.getIssueImages();
        if (issueImages != null && ! issueImages.isEmpty()) {
            dto.setThumbnail(issueImages.stream()
                    .map(IssueArticleImageDto::fromEntity)
                    .toList().get(0).getImageUrl());
        }
        return dto;
    }


    // 이슈 목록
    // 첫번째 이미지를 thumbnail에 저장
    // service 단계에서 저장 해주면 굳이 안쓰는게 나음
//    public static IssueArticleDto fromEntity(IssueArticleEntity entity) {
//        IssueArticleDto dto = new IssueArticleDto();
//        dto.setId(entity.getId());
//        dto.setTitle(entity.getTitle());
//        dto.setContent(entity.getContent());

}
