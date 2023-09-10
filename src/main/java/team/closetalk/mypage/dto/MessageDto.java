package team.closetalk.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import team.closetalk.mypage.entity.dm.Message;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MessageDto {
    private Long id;
    private String sender;
    private String content;
    private Boolean checked;

    public static MessageDto fromEntity(Message msg) {
        return MessageDto.builder()
                .id(msg.getId())
                .sender(msg.getSender().getNickname())
                .content(msg.getContent())
                .checked(msg.getChecked())
                .build();
    }
}
