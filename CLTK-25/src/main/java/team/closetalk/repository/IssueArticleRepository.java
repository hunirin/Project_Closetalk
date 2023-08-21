package team.closetalk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.entity.IssueArticleEntity;

public interface IssueArticleRepository extends JpaRepository<IssueArticleEntity, Long> {
}
