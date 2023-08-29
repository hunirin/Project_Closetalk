package team.closetalk.community.dto;

import lombok.Builder;
import lombok.Getter;
import team.closetalk.community.entity.CommunityCommentEntity;

import java.util.List;

@Getter
@Builder
public class CommunityCommentListDto {
    private String userName;
    private String content;
    private String createdAt;    // 작성 날짜
    private List<CommunityCommentReplyDto> replies;

    public static CommunityCommentListDto toCommentDto(CommunityCommentEntity comment,
                                                       List<CommunityCommentReplyDto> replies) {
        if (comment.getDeletedAt() != null) {
            return CommunityCommentListDto.builder()
                    .content("삭제된 댓글입니다.")
                    .build();
        } else if (comment.getModifiedAt() != null) {
            return CommunityCommentListDto.builder()
                    .userName(comment.getUserId().getNickname())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt() + " (수정됨)")
                    .replies(replies)
                    .build();
        } else {
            return CommunityCommentListDto.builder()
                    .userName(comment.getUserId().getNickname())
                    .content(comment.getContent())
                    .createdAt(String.valueOf(comment.getCreatedAt()))
                    .replies(replies)
                    .build();
        }
    }
}
