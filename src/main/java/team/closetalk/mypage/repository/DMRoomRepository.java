package team.closetalk.mypage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.mypage.entity.dm.DMRoom;

public interface DMRoomRepository extends JpaRepository<DMRoom, Long> {
}