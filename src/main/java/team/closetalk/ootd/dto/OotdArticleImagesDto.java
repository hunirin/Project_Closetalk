package team.closetalk.ootd.dto;

import lombok.Data;
import team.closetalk.ootd.entity.OotdArticleImagesEntity;

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
}
