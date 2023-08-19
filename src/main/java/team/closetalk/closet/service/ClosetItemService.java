package team.closetalk.closet.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.closet.dto.ClosetItemDto;
import team.closetalk.closet.entity.ClosetEntity;
import team.closetalk.closet.entity.ClosetItemEntity;
import team.closetalk.closet.repository.ClosetItemRepository;
import team.closetalk.closet.repository.ClosetRepository;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClosetItemService {
    private final ClosetItemRepository closetItemRepository;
    private final ClosetRepository closetRepository;

    // 1. 해당 옷장의 단일 아이템 조회
    public ClosetItemDto readClosetItem(Long itemId) {
        ClosetItemEntity item = closetItemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 item_id : {}", itemId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });

        log.info("[{}]의 [{}]번 아이템 조회 완료",
                item.getClosetId().getClosetName(), item.getId());
        return ClosetItemDto.toClosetItemDto(item);
    }

    public void modifyClosetItem(Long itemId, Map<String, String> itemParams) {
        ClosetItemEntity item = closetItemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 item_id : {}", itemId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });

        ClosetEntity closet = closetRepository
                .findById(Long.valueOf(itemParams.get("closetId")))
                .orElseThrow(() -> {
                    log.error("존재하지 않는 closet_id : {}", itemParams.get("closetId"));
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });

        closetItemRepository.save(item.updateEntity(itemParams, closet));
        log.info("[{}]번 아이템 수정 완료", itemId);
    }

    public void deleteClosetItem(Long itemId) {
        ClosetItemEntity item = closetItemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 item_id : {}", itemId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
        log.info("[{}]번 아이템 삭제 완료", itemId);
        closetItemRepository.deleteById(itemId);
    }
}
