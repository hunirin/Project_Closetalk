package team.closetalk.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.closet.service.EntityRetrievalService;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.entity.CommunityCommentEntity;
import team.closetalk.community.repository.CommunityArticleRepository;
import team.closetalk.community.repository.CommunityCommentRepository;
import team.closetalk.user.entity.UserEntity;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityCommentService {
    private final CommunityArticleRepository communityArticleRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final EntityRetrievalService entityRetrievalService;

    // 1. 댓글 생성
    public void createComment(Long articleId, String content,
                              Authentication authentication) {
        CommunityArticleEntity article = getArticle(articleId);
        UserEntity user = getUserEntity(authentication.getName());
        communityCommentRepository.save(new CommunityCommentEntity(content, user, article));
        log.info("댓글 등록 완료");
    }

    // 1-1. 리플 생성
    public void createReply(Long articleId, Long commentId, Authentication authentication) {
    }

    // 2. 댓글 수정
    public void updateComment(Long articleId, Long commentId, Authentication authentication) {
    }

    // 3. 댓글 삭제
    public void deleteComment(Long articleId, Long commentId, Authentication authentication) {
        // 게시글 찾기
        Optional<CommunityArticleEntity> optionalCommunity = communityArticleRepository.findById(articleId);
        if (optionalCommunity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        // 댓글 찾기
        Optional<CommunityCommentEntity> optionalCommunityComment = communityCommentRepository.findById(commentId);
        if (optionalCommunityComment.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        // 삭제
        communityCommentRepository.deleteById(commentId);
    }

    // 대댓글 삭제 -- 수정필요
    public void deleteReComment(Long articleId, Long commentId, Long reCommentId) {
        // 게시글 찾기
        Optional<CommunityArticleEntity> optionalCommunity = communityArticleRepository.findById(articleId);
        if (optionalCommunity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        // 댓글 찾기
        Optional<CommunityCommentEntity> optionalCommunityComment = communityCommentRepository.findById(commentId);
        if (optionalCommunityComment.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        // 대댓글 찾기
        Optional<CommunityCommentEntity> optionalCommunityReComment = communityCommentRepository.findById(reCommentId);
        if (optionalCommunityReComment.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        // 삭제
        communityCommentRepository.deleteById(reCommentId);
    }
    // LoginId == authentication.getName() 사용자 찾기
    private UserEntity getUserEntity(String LoginId) {
        return entityRetrievalService.getUserEntity(LoginId);
    }

    // article 찾기
    private CommunityArticleEntity getArticle(Long articleId) {
        return communityArticleRepository.findById(articleId)
                .orElseThrow(() -> {
                    log.error("{}번 게시물을 찾을 수 없음", articleId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }
}
