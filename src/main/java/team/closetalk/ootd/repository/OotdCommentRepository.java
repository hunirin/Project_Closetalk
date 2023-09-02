package team.closetalk.ootd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.ootd.entity.OotdCommentEntity;

import java.util.List;

public interface OotdCommentRepository extends JpaRepository<OotdCommentEntity, Long> {
    List<OotdCommentEntity> findAllByOotdArticle_IdAndCommentId(Long articleId, OotdCommentEntity comment);
    List<OotdCommentEntity> findAllByCommentId_Id(Long commentId);
}
