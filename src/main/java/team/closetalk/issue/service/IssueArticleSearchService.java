package team.closetalk.issue.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team.closetalk.issue.dto.IssueArticleListDto;
import team.closetalk.issue.entity.IssueArticleEntity;
import team.closetalk.issue.repository.IssueArticleSearchRepository;

@Data
@Service
@RequiredArgsConstructor
public class IssueArticleSearchService {
    private final IssueArticleSearchRepository issueArticleSearchRepository;

    // 이슈 게시물 전체 검색
    public Page<IssueArticleListDto> searchIssuePaged(String contentKeyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").ascending());

        Page<IssueArticleEntity> issueEntityPage =
                issueArticleSearchRepository.findAllByTitleOrContentOrUserId_NicknameContaining(contentKeyword, pageable);

        return issueEntityPage.map(IssueArticleListDto::fromEntity);
    }

    // 이슈 게시물 제목 검색
    public Page<IssueArticleListDto> searchTitleIssuePaged(String titleKeyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").ascending());

        Page<IssueArticleEntity> issueEntityPage =
                issueArticleSearchRepository.findAllByTitleContaining(titleKeyword, pageable);
        return issueEntityPage.map(IssueArticleListDto::fromEntity);
    }

    // 이슈 게시물 닉네임 검색
    public Page<IssueArticleListDto> searchNicknameIssuePaged (String nicknameKeyword, Integer pageNum, Integer pageSize){
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").ascending());

        Page<IssueArticleEntity> issueEntityPage =
                issueArticleSearchRepository.findAllByUserId_NicknameContaining(nicknameKeyword, pageable);
        return issueEntityPage.map(IssueArticleListDto::fromEntity);
    }
}
