package team.closetalk.mypage.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.closet.service.EntityRetrievalService;
import team.closetalk.mypage.dto.DMRoomResponseDto;
import team.closetalk.mypage.dto.MessageDto;
import team.closetalk.mypage.entity.dm.DMUser;
import team.closetalk.mypage.entity.dm.Message;
import team.closetalk.mypage.entity.dm.DMRoom;
import team.closetalk.mypage.repository.DMUserRepository;
import team.closetalk.mypage.repository.MessageRepository;
import team.closetalk.mypage.repository.DMRoomRepository;
import team.closetalk.user.entity.UserEntity;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DMService {
    private final DMRoomRepository dmRoomRepository;
    private final DMUserRepository dmUserRepository;
    private final MessageRepository messageRepository;
    private final EntityRetrievalService entityRetrievalService;

    // 방 생성
    @Transactional
    public DMRoomResponseDto createRoom(String recipientName, Authentication authentication) {
        // 1. 보내는 사람, 받는 사람
        UserEntity sender = entityRetrievalService.getUserEntity(authentication.getName());
        UserEntity recipient = entityRetrievalService.getUserEntityByNickname(recipientName);
        if (sender.equals(recipient)) throw new ResponseStatusException(HttpStatus.CONFLICT);
        Optional<DMRoom> optionalRoom = dmUserRepository.findByDMRoomWithDMUsers(sender.getId(), recipient.getId());
        // 2. 두 유저의 방이 있을 경우
        DMRoom dmRoom;
        if (optionalRoom.isPresent()) {
            dmRoom = optionalRoom.get();
            log.info("{}", dmRoom.getId());
        }
        // 3. 두 유저의 방이 없을 경우
        else {
            dmRoom = dmRoomRepository.save(new DMRoom());
            dmUserRepository.saveAll(Arrays.asList(
                    new DMUser(sender, dmRoom),
                    new DMUser(recipient, dmRoom)
            ));
        }
        return DMRoomResponseDto.fromEntity(dmRoom, recipient, checkedDirectMessageList(dmRoom.getId(), authentication));
    }

    // 방 목록
    @Transactional
    public List<DMRoomResponseDto> getRoom(Authentication authentication) {
        UserEntity user = entityRetrievalService.getUserEntity(authentication.getName());
        List<DMRoom> dmRoomList = dmUserRepository.findAllByUser(user);

        List<DMRoomResponseDto> responseDtoList = new ArrayList<>();

        for (DMRoom dmRoom : dmRoomList) {
            DMUser recipient = dmUserRepository.findByUser_LoginIdNotAndDMRoom_Id(user.getLoginId(), dmRoom.getId());
            List<Message> messageList = checkedDirectMessageList(dmRoom.getId(), authentication);
            DMRoomResponseDto dto = DMRoomResponseDto.fromEntity(dmRoom, recipient.getUser(), messageList);
            log.info("DMRoomResponseDto 생성: ID {}, Recipient: {}", dto.getId(), dto.getRecipient());
            responseDtoList.add(dto);
        }

        return responseDtoList;
    }

    // DM 전송
    public MessageDto sendDirectMessage(Long roomId, MessageDto dto,
                                        Authentication authentication) {
        DMRoom room = findRoomOr404(roomId);
        UserEntity sender = entityRetrievalService.getUserEntity(authentication.getName());

        Message msg = new Message(dto.getContent(), sender, room);
        room.addMessage(msg);
        messageRepository.save(msg);
        return MessageDto.fromEntity(msg);
    }

    // DM 목록 (Check 용)
    public List<Message> checkedDirectMessageList(Long roomId, Authentication authentication) {
        if (!dmUserRepository.existsByUser_LoginIdAndDMRoom_Id(authentication.getName(), roomId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        DMRoom room = findRoomOr404(roomId);
        List<Message> messages = messageRepository.findAllByDMRoom_Id(room.getId());

        // 변경된 메세지 상태를 저장
        messageRepository.saveAll(messages);
        return messages;
    }

    // DM 목록 (Response 용)
    public List<MessageDto> readDirectMessageList(Long roomId, Authentication authentication) {
        if (!dmUserRepository.existsByUser_LoginIdAndDMRoom_Id(authentication.getName(), roomId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        DMRoom room = findRoomOr404(roomId);
        List<Message> messages = messageRepository.findAllByDMRoom_Id(room.getId());

        // 현재 로그인한 유저
        UserEntity user = entityRetrievalService.getUserEntity(authentication.getName());

        // 읽은 메세지 목록 확인하고, sender와 현재 사용자가 동일하지 않으면 읽지 않음으로 표시
        for (Message message : messages) {
            if (!message.getSender().equals(user)) {
                // 보낸 사람과 현재 사용자가 다르면 읽음 표시
                message.setChecked(true);
            }
        }

        // 변경된 메세지 상태를 저장
        messageRepository.saveAll(messages);
        return messages.stream().map(MessageDto::fromEntity).toList();
    }

    // dm 방 or not found
    private DMRoom findRoomOr404(Long roomId) {
        Optional<DMRoom> optionalRoom = dmRoomRepository.findById(roomId);
        if (optionalRoom.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return optionalRoom.get();
    }
}
