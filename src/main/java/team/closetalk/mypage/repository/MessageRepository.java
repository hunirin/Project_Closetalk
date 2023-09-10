package team.closetalk.mypage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.mypage.entity.dm.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByDMRoom_Id(Long roomId);
}