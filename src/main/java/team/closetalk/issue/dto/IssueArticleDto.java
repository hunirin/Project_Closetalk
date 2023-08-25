package team.closetalk.issue.dto;

import lombok.Data;
import team.closetalk.issue.entity.IssueArticleEntity;
<<<<<<< HEAD

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
=======
import team.closetalk.issue.entity.IssueArticleImagesEntity;

import java.util.List;
>>>>>>> origin/CLTK-35

@Data
public class IssueArticleDto {
    private Long id;
<<<<<<< HEAD
    private Long userId;
    private String title;
    private String content;
    private String imageUrl;
    private Long hits;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static IssueArticleDto fromEntity(IssueArticleEntity entity) {
        IssueArticleDto dto = new IssueArticleDto();
        dto.setId(entity.getId());
//        dto.setUserId(entity.getUserId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setImageUrl(entity.getImageUrl());
        dto.setHits(entity.getHits());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setModifiedAt(entity.getModifiedAt());
        return dto;
    }

    private static LocalDateTime presentLocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        return instant.atZone(zoneId).toLocalDateTime();
    }


=======

    private String title;
    private String content;
    private Long hit;

    // 대표 이미지
    private String thumbnail;

    // 이슈 전체 이미지 불러오기
    private List<IssueArticleImagesDto> issueImages;

    // 이슈 목록
    // 첫번째 이미지를 thumbnail에 저장
    // service 단계에서 저장 해주면 굳이 안쓰는게 나음
    public static IssueArticleDto fromEntity(IssueArticleEntity entity) {
        IssueArticleDto dto = new IssueArticleDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());

        List<IssueArticleImagesEntity> issueImages = entity.getIssueImages();
        if (issueImages != null && ! issueImages.isEmpty()) {
            dto.setThumbnail(issueImages.stream()
                    .map(IssueArticleImagesDto::fromEntity)
                    .toList().get(0).getImageUrl());
        }
        return dto;
    }
>>>>>>> origin/CLTK-35
}
