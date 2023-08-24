package team.closetalk.closet.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.closet.dto.ClosetItemDto;
import team.closetalk.closet.entity.ClosetEntity;
import team.closetalk.closet.entity.ClosetItemEntity;
import team.closetalk.closet.repository.ClosetItemRepository;
import team.closetalk.closet.repository.ClosetRepository;
import team.closetalk.user.entity.UserEntity;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClosetItemService {
    private final ClosetItemRepository closetItemRepository;
    private final ClosetRepository closetRepository;
    private final EntityRetrievalService entityRetrievalService;

    // 1. 해당 옷장의 단일 아이템 조회
    public ClosetItemDto readClosetItem(Long itemId, Authentication authentication) {
        String nickName = getUserEntity(authentication.getName()).getNickname();
        ClosetItemEntity item = getClosetItemEntity(itemId, nickName);

        log.info("[{}]의 [{}]번 아이템 조회 완료",
                item.getClosetId().getClosetName(), item.getId());
        return ClosetItemDto.toClosetItemDto(item);
    }

    public void modifyClosetItem(Long itemId, Map<String, String> itemParams,
                                 Authentication authentication) {
        String nickName = getUserEntity(authentication.getName()).getNickname();
        ClosetItemEntity item = getClosetItemEntity(itemId, nickName);

        if (itemParams.get("changeCloset").isEmpty()) {
            log.error("존재하지 않는 closet_name : {}", itemParams.get("changeCloset"));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else {
            ClosetEntity closet = getClosetEntity(itemParams.get("changeCloset"), nickName);
            closetItemRepository.save(item.updateEntity(itemParams, closet));
            log.info("[{}]번 아이템 수정 완료", itemId);
        }
    }

    public void deleteClosetItem(Long itemId, Authentication authentication) {
        String nickName = getUserEntity(authentication.getName()).getNickname();
        ClosetEntity closetEntity = getClosetItemEntity(itemId, nickName).getClosetId();

        if (closetRepository.existsByIdAndUserId_LoginId(closetEntity.getId(), authentication.getName())) {
            closetItemRepository.deleteById(itemId);
            log.info("[{}]번 아이템 삭제 완료", itemId);
        } else {
            log.error("[{}]번 아이템이 존재하지 않음", itemId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // closetId로 해당 ClosetEntity 찾기
    private ClosetEntity getClosetEntity(String closetName, String nickName) {
        return entityRetrievalService.getClosetEntity(closetName, nickName);
    }

    // itemId로 해당 ClosetItemEntity 찾기
    private ClosetItemEntity getClosetItemEntity(Long itemId, String nickName) {
        return entityRetrievalService.getClosetItemEntity(itemId, nickName);
    }

    // LoginId == authentication.getName() 사용자 찾기
    private UserEntity getUserEntity(String LoginId) {
        return entityRetrievalService.getUserEntity(LoginId);
    }
}
