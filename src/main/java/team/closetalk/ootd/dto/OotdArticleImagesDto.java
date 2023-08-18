package team.closetalk.ootd.dto;

import lombok.Data;
import team.closetalk.ootd.entity.OotdArticleImagesEntity;

import java.util.List;

@Data
public class OotdArticleImagesDto {
    private Long id;

    private String imageUrl;

    public static OotdArticleImagesDto fromEntity(OotdArticleImagesEntity images) {
        OotdArticleImagesDto dto = new OotdArticleImagesDto();
        dto.setId(images.getId());
        dto.setImageUrl(images.getImageUrl());
        return dto;
    }

    public static String getFirstImageUrl(List<OotdArticleImagesDto> images) {
        if (images != null && !images.isEmpty()) {
            return images.get(0).getImageUrl();
        }
        return null;
    }
}
