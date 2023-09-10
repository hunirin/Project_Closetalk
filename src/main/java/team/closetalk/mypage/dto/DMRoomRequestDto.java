package team.closetalk.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class DMRoomRequestDto {
    private String recipient; // 받는 사람
}
