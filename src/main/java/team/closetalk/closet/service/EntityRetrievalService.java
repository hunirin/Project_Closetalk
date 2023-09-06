package team.closetalk.closet.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.closet.entity.ClosetEntity;
import team.closetalk.closet.entity.ClosetItemEntity;
import team.closetalk.closet.repository.ClosetItemRepository;
import team.closetalk.closet.repository.ClosetRepository;
import team.closetalk.user.entity.UserEntity;
import team.closetalk.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntityRetrievalService {
    private final ClosetRepository closetRepository;
    private final ClosetItemRepository closetItemRepository;
    private final UserRepository userRepository;

    // closetId로 해당 ClosetEntity 찾기
    public ClosetEntity getClosetEntity(String closetName, String nickname) {
        try {
            return closetRepository.findByClosetNameAndUserId_Nickname(closetName, nickname);
        } catch (Exception e) {
            log.error("{} 님의 옷장 [{}]은 존재하지 않습니다.", nickname, closetName);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // itemId로 해당 ClosetItemEntity 찾기
    public ClosetItemEntity getClosetItemEntity(Long itemId, String nickname) {
        try {
            return closetItemRepository.findByIdAndClosetId_UserId_Nickname(itemId, nickname);
        } catch (Exception e) {
            log.error("{} 님의 [{}]번 아이템은 존재하지 않습니다.", nickname, itemId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // LoginId == authentication.getName() -> UserEntity 찾기
    public UserEntity getUserEntity(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 User : {}", loginId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    // Nickname == authentication.getName() -> UserEntity 찾기
    public UserEntity getUserEntityByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 User Nickname : {}", nickname);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }
}
