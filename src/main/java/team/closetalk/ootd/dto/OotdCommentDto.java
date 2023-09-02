package team.closetalk.ootd.dto;

import lombok.Builder;
import lombok.Data;
import team.closetalk.ootd.entity.OotdCommentEntity;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Builder
public class OotdCommentDto {
    private String nickname;
    private String profile;
    private String content;
    private String createdAt;    // 작성 날짜
    private List<OotdCommentReplyDto> replies;

    public static OotdCommentDto toCommentDto(OotdCommentEntity comment,
                                              List<OotdCommentReplyDto> replies) {
        if (comment.getDeletedAt() != null) {
            return OotdCommentDto.builder()
                    .content("삭제된 댓글입니다.")
                    .build();
        } else if (comment.getModifiedAt() != null) {
            return OotdCommentDto.builder()
                    .nickname(comment.getUserEntity().getNickname())
                    .profile(comment.getUserEntity().getProfileImageUrl())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt() + " (수정됨)")
                    .replies(replies)
                    .build();
        } else {
            return OotdCommentDto.builder()
                    .nickname(comment.getUserEntity().getNickname())
                    .content(comment.getContent())
                    .profile(comment.getUserEntity().getProfileImageUrl())
                    .createdAt(String.valueOf(comment.getCreatedAt()))
                    .replies(replies)
                    .build();
        }
    }
}
