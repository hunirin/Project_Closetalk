package team.closetalk.dto;

import lombok.Data;
import team.closetalk.entity.IssueArticleEntity;

import java.time.LocalDateTime;

@Data
public class IssueArticleDto {
    private Long id;
    private Long user_id;
    private String title;
    private String content;
    private String imageUrl;
    private Long hits;
    private LocalDateTime created_at;
//    private LocalDateTime modified_at;

    public static IssueArticleDto fromEntity(IssueArticleEntity entity) {
        IssueArticleDto dto = new IssueArticleDto();
        dto.setId(entity.getId());
        dto.setUser_id(entity.getUser_id());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setImageUrl(entity.getImageUrl());
        dto.setHits(entity.getHits());
        dto.setCreated_at(entity.getCreated_at());
//        dto.setModified_at(entity.getModified_at());
        return dto;
    }
}
