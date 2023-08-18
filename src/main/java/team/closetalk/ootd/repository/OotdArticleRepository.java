package team.closetalk.ootd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.closetalk.ootd.entity.OotdArticleEntity;

public interface OotdArticleRepository extends JpaRepository<OotdArticleEntity, Long> {

    @Query("SELECT a FROM OotdArticleEntity a WHERE a.id < :cursor ORDER BY a.id DESC")
    Page<OotdArticleEntity> findByCursor(@Param("cursor") Long cursor, Pageable pageable);

}
