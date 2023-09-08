package team.closetalk.mypage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.closetalk.community.entity.CommunityLikeEntity;

import java.util.List;

public interface LikedArticleRepository extends JpaRepository<CommunityLikeEntity, Long> {
    @Query("SELECT cl.communityArticleId.id FROM CommunityLikeEntity cl WHERE cl.userId.loginId = :loginId")
    List<Long> findLikedCommunityArticleIdsByLoginId(@Param("loginId") String loginId);

    @Query("SELECT cl.ootdArticleId.id FROM OotdLikeEntity cl WHERE cl.userId.loginId = :loginId")
    List<Long> findLikedOotdArticleIdsByLoginId(@Param("loginId") String loginId);
}

