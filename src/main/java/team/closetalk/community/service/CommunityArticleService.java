package team.closetalk.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import team.closetalk.community.repository.CommunityArticleRepository;
import team.closetalk.user.entity.UserEntity;
import team.closetalk.user.repository.UserRepository;

import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommunityArticleService {
    private final CommunityArticleRepository communityArticleRepository;
    private final UserRepository userRepository;

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
    public CommunityArticleDto updateCommunityArticle(Long articleId, Authentication authentication, CommunityArticleDto dto) {
        UserEntity user = userRepository.findByLoginId(authentication.getName())
                .orElseThrow(() -> {
                    log.error("수정에 실패하였습니다.");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });

        CommunityArticleEntity communityArticle = communityArticleRepository.findByIdAndUserId(articleId, user.getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (communityArticle.getUser().getId() != user.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        communityArticle.setTitle(dto.getTitle());
        communityArticle.setContent(dto.getContent());
        communityArticle.setModifiedAt(dto.getModifiedAt());
        communityArticleRepository.save(communityArticle);

        return CommunityArticleDto.toUpdate(communityArticle);
    }

    // DELETE
    // 게시글 삭제
    public void deleteArticle(Long articleId, Authentication authentication) {
        UserEntity user = userRepository.findByLoginId(authentication.getName())
                .orElseThrow(() -> {
                    log.error("삭제에 실패하였습니다.");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
        CommunityArticleEntity communityArticle = communityArticleRepository.findByIdAndUserId(articleId, user.getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (communityArticle.getUser().getId() != user.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        communityArticleRepository.deleteById(articleId);
    }
}
