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

@Slf4j
@Service
@RequiredArgsConstructor
public class EntityRetrievalService {
    private final ClosetRepository closetRepository;
    private final ClosetItemRepository closetItemRepository;

    // closetId로 해당 ClosetEntity 찾기
    ClosetEntity getClosetEntity(Long closetId) {
        return closetRepository.findById(closetId)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 Closet_id : {}", closetId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    // itemId로 해당 ClosetItemEntity 찾기
    ClosetItemEntity getClosetItemEntity(Long itemId) {
        return closetItemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 item_id : {}", itemId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }
}
