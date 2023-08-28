package team.closetalk.closet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.closetalk.closet.entity.ClosetEntity;

import java.util.List;

@Repository
public interface ClosetRepository extends JpaRepository<ClosetEntity, Long> {
    List<ClosetEntity> findAllByClosetNameStartingWith(String closetName);
    List<ClosetEntity> findAllByUserId_LoginId(String LoginId);
    ClosetEntity findByClosetNameAndUserId_Nickname(String closetName, String nickname);
    Boolean existsByIdAndUserId_LoginId(Long closetId, String loginId);
    Boolean existsByClosetNameAndUserId_Nickname(String closetName, String nickname);
}
