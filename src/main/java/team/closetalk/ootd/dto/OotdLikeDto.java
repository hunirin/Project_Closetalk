package team.closetalk.ootd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import team.closetalk.ootd.entity.OotdLikeEntity;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class OotdLikeDto {
    private Long userId;
    private Long articleId;

    public static OotdLikeDto fromEntity(OotdLikeEntity entity) {
        return OotdLikeDto.builder()
                .userId(entity.getId())
                .articleId(entity.getOotdArticleId().getId())
                .build();
    }
}
