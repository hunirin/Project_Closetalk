package team.closetalk.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import team.closetalk.mypage.dto.DMRoomRequestDto;
import team.closetalk.mypage.dto.DMRoomResponseDto;
import team.closetalk.mypage.dto.MessageDto;
import team.closetalk.mypage.service.DMService;

import java.util.List;

@RestController
@RequestMapping("/dm")
@RequiredArgsConstructor
public class DMController {
    private final DMService dmService;

    // DM 보내기
    @PostMapping("/message/{roomId}")
    public MessageDto sendDirectMessage(@PathVariable("roomId") Long roomId,
                                        @RequestBody MessageDto dto,
                                     Authentication authentication) {
        return dmService.sendDirectMessage(roomId, dto, authentication);
    }

    // DM 목록
    @GetMapping("/message/{roomId}")
    public List<MessageDto> readDirectMessageList(@PathVariable("roomId") Long roomId,
                                                  Authentication authentication) {
        return dmService.readDirectMessageList(roomId, authentication);
    }

    // DM 방 생성
    @PostMapping("{recipient}")
    public DMRoomResponseDto createRoom(@PathVariable("recipient") DMRoomRequestDto dto,
                                        Authentication authentication) {
        return dmService.createRoom(dto.getRecipient(), authentication);
    }

    // DM 방 전체 조회
    @GetMapping
    public List<DMRoomResponseDto> getRoom(Authentication authentication) {
        return dmService.getRoom(authentication);
    }
}
