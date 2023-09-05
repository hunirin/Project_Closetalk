package team.closetalk.community.dto;

import lombok.*;
import team.closetalk.community.entity.CommunityLikeEntity;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CommunityLikeDto {
    private Long userId;
    private Long articleId;

    public static CommunityLikeDto fromEntity(CommunityLikeEntity entity) {
        return CommunityLikeDto.builder()
                .userId(entity.getId())
                .articleId(entity.getArticleId().getId())
                .build();
    }
}
