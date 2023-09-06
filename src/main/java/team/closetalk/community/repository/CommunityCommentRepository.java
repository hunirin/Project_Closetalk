package team.closetalk.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.community.entity.CommunityCommentEntity;

import java.util.List;

public interface CommunityCommentRepository extends JpaRepository<CommunityCommentEntity, Long> {
    List<CommunityCommentEntity> findAllByCommunityArticleId_IdAndCommentId(Long articleId, CommunityCommentEntity comment);
    List<CommunityCommentEntity> findAllByCommentId_Id(Long commentId);
}
