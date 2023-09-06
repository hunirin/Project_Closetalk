package team.closetalk.ootd.service;

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
import team.closetalk.closet.service.EntityRetrievalService;
import team.closetalk.ootd.dto.OotdCommentDto;
import team.closetalk.ootd.dto.OotdCommentReplyDto;
import team.closetalk.ootd.entity.OotdArticleEntity;
import team.closetalk.ootd.entity.OotdCommentEntity;
import team.closetalk.ootd.repository.OotdArticleRepository;
import team.closetalk.ootd.repository.OotdCommentRepository;
import team.closetalk.user.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OotdCommentService {
    private final OotdArticleRepository ootdArticleRepository;
    private final OotdCommentRepository ootdCommentRepository;
    private final EntityRetrievalService entityRetrievalService;

    public List<OotdCommentDto> readCommentList(Long articleId) {
        // 해당 게시물의 모든 댓글 불러오기(CommentId == null -> 대댓글이 아님)
        List<OotdCommentEntity> commentEntities =
                ootdCommentRepository.findAllByOotdArticle_IdAndCommentId(articleId, null);

        // commentList -> 반환을 위한 전체 댓글 목록 생성
        List<OotdCommentDto> commentList = new ArrayList<>();
        // 해당 게시물의 모든 댓글 중 대댓글을 찾는 반복문
        for (OotdCommentEntity commentEntity : commentEntities) {
            // commentEntities -> 이미 해당 게시물인 것을 확인
            // commentEntity.getId() -> 대댓글이 없으면 빈 리스트, 있으면 대댓글 리스트가 완성
            List<OotdCommentEntity> replies =
                    ootdCommentRepository.findAllByCommentId_Id(commentEntity.getId());

            // 대댓글의 Entity -> Dto 변환을 위한 과정
            List<OotdCommentReplyDto> replyList = new ArrayList<>();
            for (OotdCommentEntity replyEntity : replies) {
                OotdCommentReplyDto replyDto = OotdCommentReplyDto.toReplyDto(replyEntity);
                replyList.add(replyDto);
            }
            // (해당 댓글, 해당 댓글의 대댓글 Dto 리스트)를 처음에 반환을 위해 만들었던 전체 댓글 목록에 추가
            commentList.add(OotdCommentDto.toCommentDto(commentEntity, replyList));
        }
        log.info("[{}]번 게시물의 댓글 목록 조회", articleId);
        return commentList;
    }

    // 1. 댓글 생성
    public OotdCommentDto createComment(Long articleId, String content,
                                        Authentication authentication) {
        OotdArticleEntity article = getArticle(articleId);
        UserEntity user = getUserEntity(authentication.getName());
        ootdCommentRepository.save(new OotdCommentEntity(content, user, article, null));
        log.info("게시물에 댓글 등록 완료");
        return null;
    }

    // 1-1. 대댓글 생성
    public OotdCommentDto createReply(Long articleId, Long commentId,
                                      String content,
                                      Authentication authentication) {
        OotdArticleEntity article = getArticle(articleId);
        OotdCommentEntity comment = getByComment(commentId);
        UserEntity user = getUserEntity(authentication.getName());
        ootdCommentRepository.save(new OotdCommentEntity(content, user, article, comment));
        log.info("게시물의 댓글 [{}]에 댓글 등록 완료",  comment.getContent());
        return null;
    }

    // 2. 댓글 수정
    public OotdCommentDto updateComment(Long articleId, Long commentId, String content,
                                        Authentication authentication) {
        OotdArticleEntity article = getArticle(articleId);
        OotdCommentEntity comment = getByComment(commentId);
        UserEntity user = getUserEntity(authentication.getName());

        // 해당 게시물의 댓글인지 and 댓글 삭제 시도하는 사용자가 해당 댓글 작성자인지
        if (comment.getOotdArticle().equals(article) && comment.getUserEntity().equals(user)) {
            ootdCommentRepository.save(comment.updateEntity(content));
            log.info("게시물의 [{}]번 댓글 수정 완료", commentId);
        } else {
            log.error("댓글 작성자가 아닙니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    // 3. 댓글 삭제
    public OotdCommentDto deleteComment(Long articleId, Long commentId, Authentication authentication) {
        OotdArticleEntity article = getArticle(articleId);
        OotdCommentEntity comment = getByComment(commentId);
        UserEntity user = getUserEntity(authentication.getName());

        // 해당 게시물의 댓글인지 and 댓글 삭제 시도하는 사용자가 해당 댓글 작성자인지
        if (comment.getOotdArticle().equals(article) && comment.getUserEntity().equals(user)) {
            ootdCommentRepository.save(comment.deleteEntity());
            log.info("게시물의 [{}]번 댓글 삭제 완료", commentId);
        } else {
            log.error("댓글 작성자가 아닙니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    // LoginId == authentication.getName() user 찾기
    private UserEntity getUserEntity(String LoginId) {
        return entityRetrievalService.getUserEntity(LoginId);
    }

    // article 찾기
    private OotdArticleEntity getArticle(Long articleId) {
        return ootdArticleRepository.findById(articleId)
                .orElseThrow(() -> {
                    log.error("{}번 게시물을 찾을 수 없음", articleId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }

    // comment 찾기
    private OotdCommentEntity getByComment(Long commentId) {
        return ootdCommentRepository.findById(commentId)
                .orElseThrow(() -> {
                    log.error("해당 댓글 찾을 수 없음");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }
}
