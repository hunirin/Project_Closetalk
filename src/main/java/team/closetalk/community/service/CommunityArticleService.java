package team.closetalk.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.community.dto.CommunityArticleDto;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.entity.CommunityCommentEntity;
import team.closetalk.community.repository.CommunityArticleRepository;
import team.closetalk.community.repository.CommunityCommentRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommunityArticleService {
    private final CommunityArticleRepository communityArticleRepository;

    // READ
    // 페이지 단위로 조회
    public Page<CommunityArticleDto> readCommunityPaged(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNum, pageSize, Sort.by("id").ascending());
        Page<CommunityArticleEntity> communityEntityPage = communityArticleRepository.findAll(pageable);
        return communityEntityPage.map(CommunityArticleDto::fromEntity);
    }

    // 상세 페이지 조회
    public CommunityArticleDto readArticleOne(Long id) {
        Optional<CommunityArticleEntity> optionalArticle = communityArticleRepository.findById(id);

        if (optionalArticle.isPresent()) {
            return CommunityArticleDto.fromEntity2(optionalArticle.get());
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // 게시글 수정
    public CommunityArticleDto updateCommunityArticle(Long articleId, CommunityArticleDto dto, String nickname) {
        Optional<CommunityArticleEntity> optionalCommunityArticle = communityArticleRepository.findById(articleId);
        if (optionalCommunityArticle.isPresent()) {
            CommunityArticleEntity communityArticle = optionalCommunityArticle.get();
            communityArticle.setTitle(dto.getTitle());
            communityArticle.setContent(dto.getContent());
            communityArticle.setModifiedAt(dto.getModifiedAt());
            communityArticleRepository.save(communityArticle);
            return CommunityArticleDto.fromEntity(communityArticle);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // DELETE
    // 게시글 삭제
    public void deleteArticle(Long articleId, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String nickname = authentication.getName();

            // 게시글 찾기
            Optional<CommunityArticleEntity> optionalCommunity = communityArticleRepository.findById(articleId);
            if (optionalCommunity.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            CommunityArticleEntity communityArticle = optionalCommunity.get();

            // 작성자와 다를 경우 삭제 실패
            if (!communityArticle.getId().equals(nickname)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            // 삭제
            communityArticleRepository.deleteById(articleId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
