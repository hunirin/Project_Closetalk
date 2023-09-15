package team.closetalk.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import team.closetalk.mypage.entity.dm.DMRoom;
import team.closetalk.mypage.entity.dm.Message;
import team.closetalk.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class DMRoomResponseDto {
    private Long id;
    private String recipient;
    private Integer messageNotRead;
    private String currentTime;

    public static DMRoomResponseDto fromEntity(DMRoom dmRoom, UserEntity user, List<Message> messageList) {
        DMRoomResponseDto dto = new DMRoomResponseDto();
        dto.setId(dmRoom.getId());
        dto.setRecipient(user.getNickname());

        // 메세지가 없거나 메세지 리스트가 비어있는 경우
        if (messageList == null || messageList.isEmpty()) {
            dto.setMessageNotRead(0);
        } else {
            // 마지막 메세지 가져오기
            Message lastMessage = messageList.get(messageList.size() - 1);

            // 마지막 메세지를 보낸 사람이 확인하려는 사람과 같으면 messageNotRead를 0으로 설정
            if (!lastMessage.getSender().equals(user)) {
                dto.setMessageNotRead(0);
            } else {
                // 발신자가 아닌 확인되지 않은 메세지 개수 계산
                long unreadCount = messageList.stream()
                        .filter(message -> message.getSender().equals(user) && !message.getChecked())
                        .count();
                dto.setMessageNotRead((int) unreadCount);
            }
        }

        // DMRoom의 latestMessageTimestamp가 Null이면 기본값으로 현재 시간을 사용
        dto.setCurrentTime(dmRoom.getLatestMessageTimestamp() != null ?
                dmRoom.getLatestMessageTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) :
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return dto;
    }
}