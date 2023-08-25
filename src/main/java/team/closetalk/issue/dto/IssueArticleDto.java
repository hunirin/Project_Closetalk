package team.closetalk.issue.dto;

import lombok.Data;
import team.closetalk.issue.entity.IssueArticleEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import java.util.List;

@Data
public class IssueArticleDto {
    private Long id;
//<<<<<<< HEAD
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
}
