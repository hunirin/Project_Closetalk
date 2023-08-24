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
    ClosetEntity getClosetEntity(String closetName, String nickName) {
        try {
            return closetRepository.findByClosetNameAndUserId_Nickname(closetName, nickName);
        } catch (Exception e) {
            log.error("{} 님의 옷장 [{}]은 존재하지 않습니다.", nickName, closetName);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // itemId로 해당 ClosetItemEntity 찾기
    ClosetItemEntity getClosetItemEntity(Long itemId, String nickName) {
        try {
            return closetItemRepository.findByIdAndClosetId_UserId_Nickname(itemId, nickName);
        } catch (Exception e) {
            log.error("{} 님의 [{}]번 아이템은 존재하지 않습니다.", nickName, itemId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // LoginId == authentication.getName() -> UserEntity 찾기
    UserEntity getUserEntity(String LoginId) {
        return userRepository.findByLoginId(LoginId)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 User : {}", LoginId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }
}
