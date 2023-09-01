package team.closetalk.issue.dto;

import lombok.Data;
import team.closetalk.issue.entity.IssueArticleEntity;
import team.closetalk.issue.entity.IssueArticleImageEntity;

import java.util.List;

@Data
public class IssueBannerDto {
    // 이슈 목록
    // 첫번째 이미지를 thumbnail에 저장
    // service 단계에서 저장 해주면 굳이 안쓰는게 나음
    public static IssueArticleDto fromEntity(IssueArticleEntity entity) {
        IssueArticleDto dto = new IssueArticleDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());

        List<IssueArticleImageEntity> issueImages = entity.getIssueImages();
        if (issueImages != null && !issueImages.isEmpty()) {
            dto.setThumbnail(issueImages.stream()
                    .map(IssueArticleImageDto::fromEntity)
                    .toList().get(0).getImageUrl());
        }
        return dto;
    }
}
