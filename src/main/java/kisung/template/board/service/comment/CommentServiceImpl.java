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
import java.time.ZoneId;
import java.util.List;

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
  public CommentDto.PostCommentsRes createComments(CommentDto.PostCommentsReq postCommentsReq, UserInfo userInfo) {
    validate(postCommentsReq);
    Feed feed = feedService.retrieveFeedEntity(postCommentsReq.getFeedId()); // feed 조회 및 존재 여부 체크
    Comment parentComment = null;
    if (postCommentsReq.getParentCommentId() != 0) { // 부모 댓글이 존재하는 경우
      parentComment = commentRepository.findCommentById(postCommentsReq.getParentCommentId()).orElseThrow(() -> new BoardException(NON_EXIST_PARENT_COMMENT));
    }
    Comment comment = Comment.of(feed, userInfo, parentComment, postCommentsReq.getContent());
    // TODO: 레디스를 이용하여 게시글의 댓글 수를 업데이트 할 예정
    feed.increaseCommentCnt(); // 캡슐화 적용
    comment = commentRepository.save(comment);

    return CommentDto.PostCommentsRes.builder()
        .commentId(comment.getId())
        .build();
  }

  @Override
  public CommentDto.GetCommentsRes retrieveComments(CommentDto.GetCommentsReq getCommentsReq, UserInfo userInfo) {
    validate(getCommentsReq);
    // TODO: 레디스를 이용하여 게시글의 댓글 수를 조회할 예정
    Long count = commentRepository.countCommentInfos(getCommentsReq);
    List<CommentDto.CommentRawInfo> commentRawInfos = commentRepository.findCommentInfos(getCommentsReq);
    return CommentDto.GetCommentsRes.builder()
        .count(count)
        .commentInfos(makeCommentInfos(commentRawInfos))
        .build();
  }

  @Override
  public CommentDto.GetRepliesRes retrieveReplies(CommentDto.GetRepliesReq getRepliesReq, UserInfo userInfo) {
    validate(getRepliesReq);
    // TODO: 레디스를 이용하여 게시글의 댓글 수를 조회할 예정
    Long count = commentRepository.countReplyInfos(getRepliesReq);
    List<CommentDto.ReplyRawInfo> replyRawInfos = commentRepository.findReplyInfos(getRepliesReq);
    return CommentDto.GetRepliesRes.builder()
        .count(count)
        .replyInfos(makeReplyInfos(replyRawInfos))
        .build();
  }

  @Override
  @Transactional
  public CommentDto.PutCommentsRes editComments(CommentDto.PutCommentsReq putCommentsReq, UserInfo userInfo) {
    validate(putCommentsReq);
    Comment comment = commentRepository.findCommentById(putCommentsReq.getCommentId()).orElseThrow(() -> new BoardException(NON_EXIST_COMMENT));
    comment.edit(putCommentsReq.getContent());
    return CommentDto.PutCommentsRes.builder()
        .commentId(comment.getId())
        .build();
  }

  @Transactional
  @Override
  public CommentDto.DeleteCommentsRes deleteComments(CommentDto.DeleteCommentsReq deleteCommentsReq, UserInfo userInfo) {
    validate(deleteCommentsReq);
    Comment comment = commentRepository.findCommentById(deleteCommentsReq.getCommentId()).orElseThrow(() -> new BoardException(NON_EXIST_COMMENT));
    comment.delete();
    return CommentDto.DeleteCommentsRes.builder()
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
    if (postCommentsReq.getParentCommentId() == null) { // 부모 댓글 아이디 존재 여부
      throw new BoardException(NON_EXIST_PARENT_COMMENT_ID);
    }
  }

  /**
   * 댓글 조회 서비스 유효성 검사
   */
  private void validate(CommentDto.GetCommentsReq getCommentsReq) {
    if (getCommentsReq.getFeedId() == null) {
      throw new BoardException(NON_EXIST_FEED_ID);
    }
    if (getCommentsReq.getCommentId() == null) {
      throw new BoardException(NON_EXIST_COMMENT_ID);
    }
    if (getCommentsReq.getSize() == null) {
      throw new BoardException(NON_EXIST_PAGE_SIZE);
    }
  }

  /**
   * 답글 조회 서비스 유효성 검사
   */
  private void validate(CommentDto.GetRepliesReq getRepliesReq) {
    if (getRepliesReq.getCommentId() == null) {
      throw new BoardException(NON_EXIST_COMMENT_ID);
    }
    if (getRepliesReq.getParentCommentId() == null) {
      throw new BoardException(NON_EXIST_PARENT_COMMENT_ID);
    }
    if (getRepliesReq.getSize() == null) {
      throw new BoardException(NON_EXIST_PAGE_SIZE);
    }
  }

  /**
   * 댓글 수정 유효성 검사
   */
  private void validate(CommentDto.PutCommentsReq putCommentsReq) {
    if (putCommentsReq.getCommentId() == null) { // 댓글 아이디 존재 여부
      throw new BoardException(NON_EXIST_COMMENT_ID);
    }
    if (putCommentsReq.getContent() == null || putCommentsReq.getContent().isEmpty()) { // 게시글 내용 존재 여부
      throw new BoardException(NON_EXIST_COMMENT_CONTENT);
    }
    if (putCommentsReq.getContent().length() > 300) { // 게시글 내용 300자 이상인 경우
      throw new BoardException(INVALID_CONTENT);
    }
  }

  /**
   * 댓글 삭제 유효성 검사
   */
  private void validate(CommentDto.DeleteCommentsReq deleteCommentsReq) {
    if (deleteCommentsReq.getCommentId() == null) { // 댓글 아이디 존재 여부
      throw new BoardException(NON_EXIST_COMMENT_ID);
    }
  }

  private List<CommentDto.CommentInfo> makeCommentInfos(List<CommentDto.CommentRawInfo> commentRawInfos) {
    return commentRawInfos.stream().map(commentRawInfo -> CommentDto.CommentInfo.builder()
            .commentId(commentRawInfo.getCommentId())
            .childCommentCnt(commentRawInfo.getChildCommentCnt())
            .content(commentRawInfo.getStatus().equals(ACTIVE.value()) ? commentRawInfo.getContent() : "[삭제된 댓글 입니다]")
            .createdAt(commentRawInfo.getCreatedAt().atZone(ZoneId.systemDefault()).toEpochSecond())
            .updatedAt(commentRawInfo.getUpdatedAt().atZone(ZoneId.systemDefault()).toEpochSecond())
            .build()
        )
        .toList();
  }

  private List<CommentDto.ReplyInfo> makeReplyInfos(List<CommentDto.ReplyRawInfo> replyRawInfos) {
    return replyRawInfos.stream().map(replyRawInfo -> CommentDto.ReplyInfo.builder()
            .commentId(replyRawInfo.getCommentId())
            .parentCommentId(replyRawInfo.getParentCommentId())
            .content(replyRawInfo.getStatus().equals(ACTIVE.value()) ? replyRawInfo.getContent() : "[삭제된 댓글 입니다]")
            .createdAt(replyRawInfo.getCreatedAt().atZone(ZoneId.systemDefault()).toEpochSecond())
            .updatedAt(replyRawInfo.getUpdatedAt().atZone(ZoneId.systemDefault()).toEpochSecond())
            .build()
        )
        .toList();
  }
}
