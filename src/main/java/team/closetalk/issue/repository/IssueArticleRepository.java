package team.closetalk.issue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.issue.entity.IssueArticleEntity;

public interface IssueArticleRepository extends JpaRepository<IssueArticleEntity, Long> {
}
