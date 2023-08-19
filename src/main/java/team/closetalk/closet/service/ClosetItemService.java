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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClosetItemService {
    private final ClosetItemRepository closetItemRepository;

    // 1. 해당 옷장의 단일 아이템 조회
    public ClosetItemDto readClosetItem(Long itemId) {
        ClosetItemEntity item = closetItemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 item_id : {}", itemId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });

        log.info("[{}]의 [{}]번 아이템 조회 완료",
                item.getClosetId().getClosetName(), item.getId());
        return ClosetItemDto.viewClosetItem(item);
    }
}
