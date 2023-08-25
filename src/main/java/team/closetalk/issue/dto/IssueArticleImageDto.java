package team.closetalk.issue.dto;


import lombok.Data;
import team.closetalk.issue.entity.IssueArticleImageEntity;

@Data
public class IssueArticleImageDto {
    private Long id;

    private String imageUrl;

    public static IssueArticleImageDto fromEntity2(IssueArticleImageEntity images) {
        IssueArticleImageDto dto = new IssueArticleImageDto();
        dto.setId(images.getId());
        dto.setImageUrl(images.getImageUrl());
        return dto;
    }
}
