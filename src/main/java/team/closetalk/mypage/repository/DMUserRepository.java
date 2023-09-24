package team.closetalk.mypage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.closetalk.mypage.entity.dm.DMRoom;
import team.closetalk.mypage.entity.dm.DMUser;
import team.closetalk.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface DMUserRepository extends JpaRepository<DMUser, Long> {
    @Query("SELECT dmUser.DMRoom FROM DMUser dmUser " +
            "WHERE dmUser.user.id IN (:user1Id, :user2Id) GROUP BY dmUser.DMRoom " +
            "HAVING COUNT(DISTINCT dmUser.user.id) = 2")
    Optional<DMRoom> findByDMRoomWithDMUsers(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    Boolean existsByUser_LoginIdAndDMRoom_Id(String loginId, Long roomId);

    DMUser findByUser_LoginIdNotAndDMRoom_Id(String loginId, Long roomId);

    @Query("SELECT dmUser.DMRoom FROM DMUser dmUser WHERE dmUser.user = :user")
    List<DMRoom> findAllByUser(@Param("user") UserEntity user);
}
