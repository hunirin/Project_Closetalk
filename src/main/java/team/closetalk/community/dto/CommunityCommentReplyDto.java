package team.closetalk.community.dto;

import lombok.Builder;
import lombok.Getter;
import team.closetalk.community.entity.CommunityCommentEntity;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CommunityCommentReplyDto {
    private String userName;
    private String content;
    private LocalDate createdAt;    // 작성 날짜

    public static CommunityCommentReplyDto toReplyDto(CommunityCommentEntity comment) {
        return CommunityCommentReplyDto.builder()
                .userName(comment.getUserId().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
