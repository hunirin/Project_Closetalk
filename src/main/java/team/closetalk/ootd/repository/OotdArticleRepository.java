package team.closetalk.ootd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.ootd.entity.OotdArticleEntity;

public interface OotdArticleRepository extends JpaRepository<OotdArticleEntity, Long> {

}
