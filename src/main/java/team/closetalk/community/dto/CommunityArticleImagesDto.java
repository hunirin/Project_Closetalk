package team.closetalk.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import team.closetalk.community.entity.CommunityArticleImagesEntity;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CommunityArticleImagesDto {
    private Long id;

    private String imageUrl;

    public static CommunityArticleImagesDto fromEntity(CommunityArticleImagesEntity entity) {
        return CommunityArticleImagesDto.builder()
                .id(entity.getId())
                .imageUrl(entity.getImageUrl())
                .build();
    }
}
