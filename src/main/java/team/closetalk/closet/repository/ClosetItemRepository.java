package team.closetalk.closet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.closetalk.closet.entity.ClosetItemEntity;

import java.util.List;

public interface ClosetItemRepository extends JpaRepository<ClosetItemEntity, Long> {
    List<ClosetItemEntity> findAllByClosetId_UserId_Nickname(String nickname);
    List<ClosetItemEntity> findAllByClosetId_Id(Long closetId);
    List<ClosetItemEntity> findAllByClosetId_IdAndCategory(Long closetId, String category);
    ClosetItemEntity findByIdAndClosetId_UserId_Nickname(Long itemId, String nickname);

    @Modifying
    @Query(value = "DELETE FROM closetalk_item WHERE closet_item_id " +
            "IN (SELECT closet_item_id FROM closet_item WHERE closet_id = :closetId)", nativeQuery = true)
    void deleteAllByClosetId(@Param("closetId") Long closetId);

    @Modifying
    @Query(value = "DELETE FROM closet_item WHERE closet_id = :closetId", nativeQuery = true)
    void deleteClosetItemByClosetId(@Param("closetId") Long closetId);

}