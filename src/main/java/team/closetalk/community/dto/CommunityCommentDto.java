package team.closetalk.community.dto;

import lombok.Builder;
import lombok.Getter;
import team.closetalk.community.entity.CommunityCommentEntity;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
public class CommunityCommentDto {
    private Long id;
    private String nickname;
    private String content;
    private String createdAt;    // 작성 날짜
    private String profile;
    private List<CommunityCommentReplyDto> replies;

    public static CommunityCommentDto toCommentDto(CommunityCommentEntity comment,
                                                   List<CommunityCommentReplyDto> replies) {
        if (comment.getDeletedAt() != null) {
            return CommunityCommentDto.builder()
                    .content("삭제된 댓글입니다.")
                    .build();
        } else if (comment.getModifiedAt() != null) {
            return CommunityCommentDto.builder()
                    .id(comment.getId())
                    .nickname(comment.getUserId().getNickname())
                    .profile(comment.getUserId().getProfileImageUrl())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " (수정됨)")
                    .replies(replies)
                    .build();
        } else {
            return CommunityCommentDto.builder()
                    .id(comment.getId())
                    .nickname(comment.getUserId().getNickname())
                    .profile(comment.getUserId().getProfileImageUrl())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                    .replies(replies)
                    .build();
        }
    }
}
