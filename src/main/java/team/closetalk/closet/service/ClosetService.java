package team.closetalk.closet.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.closet.dto.ClosetDto;
import team.closetalk.closet.dto.ClosetItemDto;
import team.closetalk.closet.entity.ClosetEntity;
import team.closetalk.closet.entity.ClosetItemEntity;
import team.closetalk.closet.repository.ClosetItemRepository;
import team.closetalk.closet.repository.ClosetRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClosetService {
    private final ClosetRepository closetRepository;
    private final ClosetItemRepository closetItemRepository;

    // 1. 옷장 목록 조회(이름, 공개 여부)
    public List<ClosetDto> findCloset() {
        List<ClosetEntity> closetEntities = closetRepository.findAll();
        log.info("가지고 있는 옷장 목록 조회 완료");
        return closetEntities.stream().map(ClosetDto::viewCloset).toList();
    }

    // 1-1. 옷장 생성
    public void addCloset(String closetName, Boolean isHidden) {
        if (closetRepository.findAll().size() == 5) {
            log.error("옷장 최대 생성 개수 제한");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // 옷장 이름이 없으면 Closet (마지막 생성 옷장의 ClosetId + 1)으로 생성
        if (closetName == null || closetName.equals(""))
            closetName = String.format("My Closet %d",
                        closetRepository.findTopByOrderByIdDesc().getId() + 1);
        closetRepository.save(new ClosetDto().newCloset(closetName, isHidden));
        log.info("옷장 생성 완료");
    }

    // 1-2. 옷장 삭제 (해당 옷장 내 모든 아이템 포함)
    @Transactional // 메서드 내 모든 작업 중 하나라도 실패 시 전체 작업 취소
    public void removeCloset(Long closetId) {
        String closetName = closetRepository.findById(closetId)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 Closet_id : {}", closetId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                })
                .getClosetName();

        // 해당 옷장 내 모든 아이템 삭제
        closetItemRepository.deleteAllByClosetId_Id(closetId);

        // 해당 옷장 삭제
        closetRepository.deleteById(closetId);
        log.info("{} 삭제 완료", closetName);
    }

    // 2. 해당 옷장의 아이템 목록 조회
    public List<ClosetItemDto> readByCloset(Long closetId) {
        ClosetEntity closet = closetRepository.findById(closetId)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 Closet_id : {}", closetId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
        List<ClosetItemEntity> itemEntities =
                closetItemRepository.findAllByClosetId_Id(closet.getId());
        log.info("{}의 아이템 목록 조회 완료", closet.getClosetName());
        return itemEntities.stream().map(ClosetItemDto::viewClosetItem).toList();
    }

    // 3. 해당 옷장의 카테고리 별 아이템 목록 조회
    public List<ClosetItemDto> readByCategory(Long closetId, String category) {
        ClosetEntity closet = closetRepository.findById(closetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<ClosetItemEntity> itemEntities =
                closetItemRepository.findAllByClosetId_IdAndCategory(closet.getId(), category);
        log.info("{}의 {} 별 아이템 목록 조회 완료", closet.getClosetName(), category);
        return itemEntities.stream().map(ClosetItemDto::viewClosetItem).toList();
    }
}
