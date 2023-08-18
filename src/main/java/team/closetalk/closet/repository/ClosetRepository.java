package team.closetalk.closet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.closetalk.closet.entity.ClosetEntity;

@Repository
public interface ClosetRepository extends JpaRepository<ClosetEntity, Long> {
    ClosetEntity findTopByOrderByIdDesc();
}
