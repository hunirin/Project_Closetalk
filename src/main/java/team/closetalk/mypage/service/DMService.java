package team.closetalk.mypage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.mypage.dto.DMRoomDto;
import team.closetalk.mypage.dto.MessageDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DMService {
    public DMRoomDto createDMRoom(Long loginId) {
        throw new UnsupportedOperationException();
    }

    public List<DMRoomDto> getDMList() {
        throw new UnsupportedOperationException();
    }

    public MessageDto sendDirectMessage(Long loginId, MessageDto messageDto) {
        throw new UnsupportedOperationException();
    }
}
