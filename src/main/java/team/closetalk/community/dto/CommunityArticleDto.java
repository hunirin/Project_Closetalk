package team.closetalk.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.enumeration.CommunityCategoryEnum;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CommunityArticleDto {
    private Long id;
    private CommunityCategoryEnum category;    // 카테고리
    private String title;       // 제목
    private String content;     // 내용
    private Long hits;          // 조회수
    private String createdAt;    // 작성 날짜

    private List<CommunityArticleImagesDto> communityImages;
    private List<CommunityCommentDto> communityComments;

    // 게시글 상세 조회
    public static CommunityArticleDto detailFromEntity(CommunityArticleEntity entity,
                                                       List<CommunityCommentDto> commentDtoList,
                                                       List<CommunityArticleImagesDto> imageDtoList) {
        CommunityArticleDto dto = new CommunityArticleDto();
        dto.setId(entity.getId());
        dto.setCategory(entity.getCategory());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setHits(entity.getHits());
        dto.setCommunityComments(commentDtoList);
        dto.setCommunityImages(imageDtoList);

        if (entity.getModifiedAt() == null) // 수정 시 created_at 변경
            dto.setCreatedAt(entity.getCreatedAt()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        else
            dto.setCreatedAt(entity.getCreatedAt()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " (수정됨)");
        return dto;
    }
}
