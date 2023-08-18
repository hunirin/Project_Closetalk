package team.closetalk.closet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.closet.entity.ClosetItemEntity;

import java.util.List;

public interface ClosetItemRepository extends JpaRepository<ClosetItemEntity, Long> {
    List<ClosetItemEntity> findAllByClosetId_Id(Long closetId);
    List<ClosetItemEntity> findAllByClosetId_IdAndCategory(Long closetId, String category);
    void deleteAllByClosetId_Id(Long closetId);
}