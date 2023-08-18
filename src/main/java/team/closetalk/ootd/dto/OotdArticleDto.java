package team.closetalk.ootd.dto;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NonNull;
import team.closetalk.ootd.entity.OotdArticleEntity;
import team.closetalk.ootd.entity.OotdArticleImagesEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OotdArticleDto {
    private Long id;

    private String nickname;
    private String content;
    private String hashtag;

//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//    @Column(name = "modified_at")
//    private LocalDateTime modifiedAt;

    //    private UserEntity user;

    // 피드 대표 이미지
    private String thumbnail;

    // 피드 전체 이미지
    private List<OotdArticleImagesDto> ootdImages;

    // 피드 생성
    public OotdArticleDto newEntity(OotdArticleEntity entity) {
        OotdArticleDto dto = new OotdArticleDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setHashtag(entity.getHashtag());
//        dto.setCreatedAt(LocalDateTime.parse(createdAt.format(DateTimeFormatter.ofPattern("MM월 dd일(E)"))));
        return dto;
    }

    // 피드 목록
    public static OotdArticleDto fromEntity(OotdArticleEntity entity) {
        OotdArticleDto dto = new OotdArticleDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setHashtag(entity.getHashtag());

        List<OotdArticleImagesEntity> ootdImages = entity.getOotdImages();
        if (ootdImages != null && !ootdImages.isEmpty()) {
            dto.setThumbnail(ootdImages.stream().map(OotdArticleImagesDto::fromEntity).toList().get(0).getImageUrl());
        }
        System.out.println(dto.getThumbnail());

        return dto;
    }
}
