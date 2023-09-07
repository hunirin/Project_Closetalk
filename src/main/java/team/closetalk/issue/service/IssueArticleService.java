package team.closetalk.issue.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.closet.service.EntityRetrievalService;
import team.closetalk.issue.dto.IssueArticleDto;
import team.closetalk.issue.dto.IssueArticleListDto;
import team.closetalk.issue.dto.IssueBannerDto;
import team.closetalk.issue.dto.IssueCreateArticleDto;
import team.closetalk.issue.entity.IssueArticleEntity;
import team.closetalk.issue.entity.IssueArticleImageEntity;
import team.closetalk.issue.repository.IssueArticleImageRepository;
import team.closetalk.issue.repository.IssueArticleRepository;
import team.closetalk.user.entity.UserEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class IssueArticleService {
    private final IssueArticleRepository issueArticleRepository;
    private final EntityRetrievalService entityRetrievalService;

    public IssueArticleDto createArticle(IssueCreateArticleDto dto,
                                         Authentication authentication) {
        UserEntity user = getUserEntity(authentication.getName());
        IssueArticleEntity article =
                new IssueArticleEntity(dto.getCategory(), dto.getTitle(), dto.getContent(), user);
        issueArticleRepository.save(article);
        return readArticle(article.getId());
    }

    // 이슈 이미지 업로드 -- 수정필요
    // 1. thumbnail에 첫번째 사진 저장하기
    // 2. imageUrl의 첫번째 사진 불러오기 -> html에 연결
    public List<IssueArticleDto> uploadIssueImg(Long id, List<MultipartFile> issueImages) {

        // 1. 유저 확인
        Optional<IssueArticleEntity> optionalUser = issueArticleRepository.findById(id);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        IssueArticleEntity issueArticleEntity = optionalUser.get();
        List<IssueArticleDto> uploadedIssueArticles = new ArrayList<>();

        for (MultipartFile issueImage : issueImages) {
            // 2-1. 폴더 만들기
            String issueDir = String.format("src/main/resources/static/images/issue/%d/", id);
            log.info(issueDir);
            try {
                Files.createDirectories(Path.of(issueDir));

            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // 2-2. 확장자를 포함한 이미지 이름 만들기 (issue.{확장자})
            String originalFilename = issueImage.getOriginalFilename();
            // issue.jpg => fileNameSplit = {"issue_img, "jpg"}
            String[] fileNameSplit = originalFilename.split("\\.");
            String extension = fileNameSplit[fileNameSplit.length - 1];
            String issueFilename = "issue." + extension;
            log.info(issueFilename);

            // 2-3. 폴더와 파일 경로를 포함한 이름 만들기
            String issuePath = issueDir + issueFilename;
            log.info(issuePath);

            // 3. MultiPartFile 저장하기
            try {
                issueImage.transferTo(Path.of(issuePath));
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // 4. UserEntity 업데이트
            // http://localhost:8080/static/1/issueImage.jpg
            log.info(String.format("/images/issue/%d/%s", id, issueFilename));


//            issueArticleEntity.setImageUrl(String.format("/images/issue/%d/%s", id, issueFilename));

            IssueArticleEntity savedIssueArticle = issueArticleRepository.save(issueArticleEntity);
            uploadedIssueArticles.add(IssueArticleDto.fromEntity(savedIssueArticle));
        }
        return uploadedIssueArticles;
    }

    public IssueArticleDto readArticle(Long articleId) {
        IssueArticleEntity article = issueArticleRepository.findById(articleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (article.getDeletedAt() != null) {
            log.error("삭제된 게시물입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // 조회수 증가
        issueArticleRepository.save(article.increaseHit());
        return IssueArticleDto.fromEntity(article);
    }

    public Page<IssueArticleListDto> readIssuePaged(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").ascending());

        Page<IssueArticleEntity> issueArticleEntityPage =
                issueArticleRepository.findAllByDeletedAtIsNull(pageable);

        return issueArticleEntityPage.map(IssueArticleListDto::fromEntity);
    }

    // 게시글 수정
    public IssueArticleDto updateArticle(Long articleId,
                                         Authentication authentication,
                                         IssueArticleDto dto) {
        UserEntity user = getUserEntity(authentication.getName());

        IssueArticleEntity article = issueArticleRepository.findByIdAndUserId_Id(articleId, user.getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setModifiedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        issueArticleRepository.save(article);

        return readArticle(articleId);
    }


    public void deleteArticle(Long articleId, Authentication authentication) {
        UserEntity user = getUserEntity(authentication.getName());
        IssueArticleEntity article = issueArticleRepository.findByIdAndUserId_Id(articleId, user.getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!Objects.equals(article.getUserId().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        issueArticleRepository.save(article.deleteArticle());
    }


    public UserEntity getUserEntity(String loginId) {
        return entityRetrievalService.getUserEntity(loginId);
    }
}
