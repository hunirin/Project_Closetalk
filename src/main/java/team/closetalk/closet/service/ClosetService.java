package team.closetalk.closet.service;

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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
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

    // Test Data
    public ClosetService(ClosetRepository closetRepository,
                         ClosetItemRepository closetItemRepository) {
        this.closetRepository = closetRepository;
        this.closetItemRepository = closetItemRepository;

        ClosetEntity closet = new ClosetEntity();
        closet.setClosetName("My Closet 1");
        closet.setIsHidden(false);
        this.closetRepository.save(closet);

        ClosetItemEntity items = new ClosetItemEntity();
        items.setBrand("브랜드1");
        items.setCategory("TOP");
        items.setItemImageUrl("/image1.png");
        items.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        items.setClosetId(closet);
        this.closetItemRepository.save(items);

        ClosetItemEntity items2 = new ClosetItemEntity();
        items2.setBrand("브랜드2");
        items2.setCategory("OUTER");
        items2.setItemImageUrl("/image2.png");
        items2.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        items2.setClosetId(closet);
        this.closetItemRepository.save(items2);

        ClosetItemEntity item3 = new ClosetItemEntity();
        item3.setBrand("브랜드1");
        item3.setCategory("OUTER");
        item3.setItemImageUrl("/image3.png");
        item3.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        item3.setClosetId(closet);
        this.closetItemRepository.save(item3);
    }
}
