package team.closetalk.closet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
    }

    public List<ClosetItemDto> readByCloset(Long closetId) {
        ClosetEntity closet = closetRepository.findById(closetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<ClosetItemEntity> itemEntities =
                closetItemRepository.findAllByClosetId_Id(closet.getId());
        return itemEntities.stream().map(ClosetItemDto::viewCloset).toList();
    }

    public void readByCategory() {
    }
}
