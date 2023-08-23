package team.closetalk.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.entity.CommunityCommentEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CommunityCommentDto {
    private Long id;

    private String content;             // 내용

//    private LocalDateTime createdAt;    // 작성 날짜
//    private LocalDateTime modifiedAt;   // 수정 날짜

    private CommunityArticleEntity articleId;
    private CommunityCommentEntity commentId;

    // 댓글 생성
    public CommunityCommentDto newEntity() {
        return CommunityCommentDto.builder()
                .content(content)
                .articleId(articleId)
                .commentId(commentId)
//                .createdAt(LocalDateTime.now(ZoneId.of("Asia/seoul")))
                .build();
    }

    // 댓글 조회
    public static CommunityCommentDto fromEntity(CommunityCommentEntity entity) {
        return CommunityCommentDto.builder()
                .content(entity.getContent())
//                .createdAt(LocalDateTime.parse(entity.getCreatedAt()
//                        .format(DateTimeFormatter.ofPattern("MM월 dd일"))))
                .build();
    }
}
