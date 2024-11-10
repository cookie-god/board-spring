package kisung.template.board.service.comment;

import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.CommentDto;
import kisung.template.board.entity.Comment;
import kisung.template.board.entity.Feed;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.repository.comment.CommentRepository;
import kisung.template.board.service.feed.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static kisung.template.board.common.code.ErrorCode.*;
import static kisung.template.board.enums.Status.ACTIVE;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
  private final FeedService feedService;
  private final CommentRepository commentRepository;

  @Transactional
  @Override
  public CommentDto.PostCommetsRes createComments(CommentDto.PostCommentsReq postCommentsReq, UserInfo userInfo) {
    validate(postCommentsReq);
    Feed feed = feedService.retrieveFeedEntity(postCommentsReq.getFeedId()); // feed 조회 및 존재 여부 체크
    Comment parentComment = null;
    if (postCommentsReq.getParentCommentId() != 0) { // 부모 댓글이 존재하는 경우
      parentComment = commentRepository.findCommentById(postCommentsReq.getParentCommentId()).orElseThrow(() -> new BoardException(NON_EXIST_PARENT_COMMENT));
    }
    Comment comment = makeCommentEntity(feed, userInfo, parentComment, postCommentsReq.getContent());
    // TODO: 레디스를 이용하여 게시글의 댓글 수를 업데이트 할 예정
    feed.upCommentCnt();
    comment = commentRepository.save(comment);

    return CommentDto.PostCommetsRes.builder()
        .commentId(comment.getId())
        .build();
  }

  /**
   * 댓글 생성 유효성 검사
   */
  private void validate(CommentDto.PostCommentsReq postCommentsReq) {
    if (postCommentsReq.getFeedId() == null) { // 피드 게시글 아이디 존재 여부
      throw new BoardException(NON_EXIST_FEED_ID);
    }
    if (postCommentsReq.getContent() == null || postCommentsReq.getContent().isEmpty()) { // 게시글 내용 존재 여부
      throw new BoardException(NON_EXIST_COMMENT_CONTENT);
    }
    if (postCommentsReq.getContent().length() > 300) { // 게시글 내용 300자 이상인 경우
      throw new BoardException(INVALID_CONTENT);
    }
  }

  private Comment makeCommentEntity(Feed feed, UserInfo userInfo, Comment parentCommnet, String content) {
    LocalDateTime now = LocalDateTime.now();
    return Comment.builder()
        .content(content)
        .bookmarkCnt(0L)
        .feed(feed)
        .userInfo(userInfo)
        .parent(parentCommnet)
        .createdAt(now)
        .updatedAt(now)
        .status(ACTIVE.value())
        .build();
  }
}
