package team.closetalk.community.dto;

import lombok.Builder;
import lombok.Getter;
import team.closetalk.community.entity.CommunityCommentEntity;

@Getter
@Builder
public class CommunityCommentReplyDto {
    private String userName;
    private String content;
    private String createdAt;    // 작성 날짜

    public static CommunityCommentReplyDto toReplyDto(CommunityCommentEntity comment) {
        if (comment.getDeletedAt() != null) {
            return CommunityCommentReplyDto.builder()
                    .content("삭제된 댓글입니다.")
                    .build();
        } else if (comment.getModifiedAt() != null) {
            return CommunityCommentReplyDto.builder()
                    .userName(comment.getUserId().getNickname())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt() + " (수정됨)")
                    .build();
        } else {
            return CommunityCommentReplyDto.builder()
                    .userName(comment.getUserId().getNickname())
                    .content(comment.getContent())
                    .createdAt(String.valueOf(comment.getCreatedAt()))
                    .build();
        }
    }
}
