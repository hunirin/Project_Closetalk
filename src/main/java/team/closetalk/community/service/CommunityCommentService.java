package team.closetalk.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team.closetalk.community.entity.CommunityArticleEntity;
import team.closetalk.community.entity.CommunityCommentEntity;
import team.closetalk.community.repository.CommunityArticleRepository;
import team.closetalk.community.repository.CommunityCommentRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityCommentService {
    private final CommunityArticleRepository communityArticleRepository;
    private final CommunityCommentRepository communityCommentRepository;

    // 댓글 삭제
    public void deleteComment(Long articleId, Long commentId) {
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
}
