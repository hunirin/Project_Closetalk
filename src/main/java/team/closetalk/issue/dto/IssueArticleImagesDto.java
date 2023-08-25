package team.closetalk.issue.dto;


import lombok.Data;
import team.closetalk.issue.entity.IssueArticleImagesEntity;

@Data
public class IssueArticleImagesDto {
    private Long id;

    private String imageUrl;

    public static IssueArticleImagesDto fromEntity(IssueArticleImagesEntity images) {
        IssueArticleImagesDto dto = new IssueArticleImagesDto();
        dto.setId(images.getId());
        dto.setImageUrl(images.getImageUrl());
        return dto;
    }
}
