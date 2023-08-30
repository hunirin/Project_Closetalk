package team.closetalk.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.entity.CommunityArticleImagesEntity;
import team.closetalk.community.entity.CommunityCommentEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CommunityArticleDto {
    private Long id;

    private String category;    // 카테고리
    private String title;       // 제목
    private String content;     // 내용
    private Long hits;          // 조회수
    private String thumbnail;   // 대표이미지
    private LocalDate createdAt;    // 작성 날짜
    private LocalDate modifiedAt;    // 수정 날짜

    private List<CommunityArticleImagesDto> communityImages;
    private List<CommunityCommentDto> communityComments;

    // 게시글 생성
    public CommunityArticleDto newEntity(
            String category,
            String title,
            String content
    ) {
        return CommunityArticleDto.builder()
                .category(category)
                .title(title)
                .content(content)
                .createdAt(LocalDate.now())
                .build();
    }

    // 게시글 목록 조회
    public static CommunityArticleDto fromEntity(CommunityArticleEntity entity) {
        return CommunityArticleDto.builder()
                .category(entity.getCategory())
                .title(entity.getTitle())
                .content(entity.getContent())
                .hits(entity.getHits())
                .thumbnail(entity.getThumbnail())
                .createdAt(entity.getCreatedAt().atStartOfDay().toLocalDate())
                .build();
    }

    // 게시글 상세 조회
    public static CommunityArticleDto fromEntity2(CommunityArticleEntity entity) {
        CommunityArticleDto dto = new CommunityArticleDto();
        dto.setId(entity.getId());
        dto.setCategory(entity.getCategory());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setCreatedAt(entity.getCreatedAt());
        // 이미지 전체 불러오기
        List<CommunityArticleImagesEntity> communityImages = entity.getCommunityImages();
        if (communityImages != null && !communityImages.isEmpty()) {
            dto.setCommunityImages(communityImages.stream().map(CommunityArticleImagesDto::fromEntity).collect(Collectors.toList()));
        }
        // 댓글 불러오기
        List<CommunityCommentEntity> comments = entity.getCommunityComments();
        if (comments != null && !comments.isEmpty()) {
            dto.setCommunityComments(comments.stream().map(CommunityCommentDto::fromEntity).collect(Collectors.toList()));
        }
        return dto;
    }

    public static CommunityArticleDto toUpdate(CommunityArticleEntity entity) {
        CommunityArticleDto dto = new CommunityArticleDto();
        dto.setId(entity.getId());
        dto.setCategory(entity.getCategory());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setModifiedAt(entity.getModifiedAt());
        return dto;
    }
}
