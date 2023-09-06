package team.closetalk.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.closetalk.mypage.dto.DMRoomDto;
import team.closetalk.mypage.dto.MessageDto;
import team.closetalk.mypage.service.DMService;

import java.util.List;

@RestController
@RequestMapping("/dm")
@RequiredArgsConstructor
public class DMController {
    private final DMService dmService;

    // DM 생성
    @PostMapping("/{loginId}")
    public DMRoomDto createDMRoom(@PathVariable("loginId") Long loginId) {
        return dmService.createDMRoom(loginId);
    }

    // DM 목록
    @GetMapping("/")
    public List<DMRoomDto> getDMRooms() {
        return dmService.getDMList();
    }

    // DM 보내기
    @PostMapping("/{loginId}/send")
    public MessageDto sendDirectMessage(
            @PathVariable("loginId") Long loginId,
            @RequestBody MessageDto messageDto
    ) {
        return dmService.sendDirectMessage(loginId, messageDto);
    }
}
