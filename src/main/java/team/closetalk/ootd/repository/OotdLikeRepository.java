package team.closetalk.ootd.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.ootd.entity.OotdArticleEntity;
import team.closetalk.ootd.entity.OotdLikeEntity;
import team.closetalk.user.entity.UserEntity;

import java.util.Optional;

public interface OotdLikeRepository extends JpaRepository<OotdLikeEntity, Long> {
    Optional<OotdLikeEntity> findByUserIdAndOotdArticleId(UserEntity userId, OotdArticleEntity articleId);
    
}
