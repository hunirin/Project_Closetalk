package team.closetalk.issue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.issue.entity.IssueArticleImagesEntity;

public interface IssueArticleImagesRepository extends JpaRepository<IssueArticleImagesEntity, Long> {
}
