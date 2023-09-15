package team.closetalk.issue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team.closetalk.issue.dto.IssueArticleDto;
import team.closetalk.issue.dto.IssueBannerDto;
import team.closetalk.issue.entity.IssueArticleEntity;
import team.closetalk.issue.repository.IssueArticleRepository;

@Service
@RequiredArgsConstructor
public class IssueBannerService {
    private final IssueArticleRepository issueArticleRepository;
    // 마지막 세개만 조회
    public Page<IssueArticleDto> readLastThreeIssues() {
        Pageable pageable = PageRequest.of(0, 3, Sort.by("id").descending());
        Page<IssueArticleEntity> issueEntityPage = issueArticleRepository.findAll(pageable);
        return issueEntityPage.map(IssueBannerDto::fromEntity);
    }
}
