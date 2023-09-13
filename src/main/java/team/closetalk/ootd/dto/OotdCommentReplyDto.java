package team.closetalk.ootd.dto;

import lombok.Builder;
import lombok.Data;
import team.closetalk.ootd.entity.OotdCommentEntity;

import java.time.format.DateTimeFormatter;

@Data
@Builder
public class OotdCommentReplyDto {
    private String nickname;
    private String profile;
    private String content;
    private String createdAt;    // 작성 날짜

    public static OotdCommentReplyDto toReplyDto(OotdCommentEntity comment) {
        if (comment.getDeletedAt() != null) {
            return OotdCommentReplyDto.builder()
                    .content("삭제된 댓글입니다.")
                    .build();
        } else if (comment.getModifiedAt() != null) {
            return OotdCommentReplyDto.builder()
                    .nickname(comment.getUserEntity().getNickname())
                    .profile(comment.getUserEntity().getProfileImageUrl())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +" (수정됨)")
                    .build();
        } else {
            return OotdCommentReplyDto.builder()
                    .nickname(comment.getUserEntity().getNickname())
                    .profile(comment.getUserEntity().getProfileImageUrl())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                    .build();
        }
    }
}
