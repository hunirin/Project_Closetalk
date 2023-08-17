package team.closetalk.closet.service;

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

@Service
public class ClosetService {
    private final ClosetRepository closetRepository;
    private final ClosetItemRepository closetItemRepository;

    // 1. 옷장 리스트(이름, 공개 여부)
    public List<ClosetDto> readCloset() {
        List<ClosetEntity> closetEntities = closetRepository.findAll();
        return closetEntities.stream().map(ClosetDto::viewCloset).toList();
    }
    // 2. 해당 옷장의 아이템 List 불러오기
    public List<ClosetItemDto> readByCloset(Long closetId) {
        ClosetEntity closet = closetRepository.findById(closetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<ClosetItemEntity> itemEntities =
                closetItemRepository.findAllByClosetId_Id(closet.getId());
        return itemEntities.stream().map(ClosetItemDto::viewClosetItem).toList();
    }

    public List<ClosetItemDto> readByCategory(Long closetId, String category) {
        ClosetEntity closet = closetRepository.findById(closetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<ClosetItemEntity> itemEntities =
                closetItemRepository.findAllByClosetId_IdAndCategory(closet.getId(), category);
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
