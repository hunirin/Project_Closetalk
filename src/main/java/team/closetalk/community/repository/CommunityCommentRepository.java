package team.closetalk.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.community.entity.CommunityCommentEntity;

public interface CommunityCommentRepository extends JpaRepository<CommunityCommentEntity, Long> {
}
