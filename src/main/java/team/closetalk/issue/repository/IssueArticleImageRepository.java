package team.closetalk.issue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.closetalk.issue.entity.IssueArticleImageEntity;

import java.util.List;

@Repository
public interface IssueArticleImageRepository extends JpaRepository<IssueArticleImageEntity, Long> {

    List<IssueArticleImageEntity> findAllByIssueArticleId_Id(Long articleId);
}
