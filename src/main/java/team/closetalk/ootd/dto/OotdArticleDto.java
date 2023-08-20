package team.closetalk.ootd.dto;

import lombok.Data;
import team.closetalk.ootd.entity.OotdArticleEntity;
import team.closetalk.ootd.entity.OotdArticleImagesEntity;
import java.util.List;

@Data
public class OotdArticleDto {
    private Long id;

    private String nickname;
    private String content;
    private String hashtag;

    // 피드 대표 이미지
    private String thumbnail;

    // 피드 전체 이미지
    private List<OotdArticleImagesDto> ootdImages;

    // 피드 목록
    public static OotdArticleDto fromEntity(OotdArticleEntity entity) {
        OotdArticleDto dto = new OotdArticleDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setHashtag(entity.getHashtag());

        // 첫번째 이미지를 thumbnail 저장
        // service 단계에서 저장해주면 굳이 안써도됨
        List<OotdArticleImagesEntity> ootdImages = entity.getOotdImages();
        if (ootdImages != null && !ootdImages.isEmpty()) {
            dto.setThumbnail(ootdImages.stream().map(OotdArticleImagesDto::fromEntity).toList().get(0).getImageUrl());
        }
        System.out.println(dto.getThumbnail());

        return dto;
    }
}
