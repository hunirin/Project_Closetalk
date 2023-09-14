package team.closetalk.issue.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import team.closetalk.issue.entity.IssueArticleImageEntity;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class IssueArticleImageDto {
    private Long id;

    private String imageUrl;

    public static IssueArticleImageDto fromEntity(IssueArticleImageEntity entity) {
        return IssueArticleImageDto.builder()
                .id(entity.getId())
                .imageUrl(entity.getImageUrl())
                .build();
    }
}
