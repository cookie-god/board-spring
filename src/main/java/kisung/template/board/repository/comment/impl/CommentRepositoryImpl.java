package kisung.template.board.repository.comment.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kisung.template.board.dto.CommentDto;
import kisung.template.board.entity.Comment;
import kisung.template.board.entity.QComment;
import kisung.template.board.repository.comment.custom.CustomCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static kisung.template.board.entity.QComment.comment;
import static kisung.template.board.enums.Status.ACTIVE;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Optional<Comment> findCommentById(Long commentId) {
    return Optional.ofNullable(
      jpaQueryFactory
        .select(comment)
        .from(comment)
        .where(
          comment.id.eq(commentId),
          comment.status.eq(ACTIVE.value())
        )
        .fetchFirst()
    );
  }

  @Override
  public Long countCommentInfos(CommentDto.GetCommentsReq getCommentsReq) {
    return jpaQueryFactory
      .select(comment.count())
      .from(comment)
      .where(
        retrieveCommentCondition(getCommentsReq.getFeedId())
      )
      .fetchFirst();
  }

  @Override
  public List<CommentDto.CommentRawInfo> findCommentInfos(CommentDto.GetCommentsReq getCommentsReq) {
    QComment childComment = new QComment("childComment");
    return jpaQueryFactory
      .select(
        Projections.bean(
          CommentDto.CommentRawInfo.class,
          comment.id.as("commentId"),
          childComment.id.count().as("childCommentCnt"),
          comment.content,
          comment.createdAt,
          comment.updatedAt
        )
      )
      .from(comment).leftJoin(childComment).on(comment.id.eq(childComment.parent.id).and(childComment.status.eq(ACTIVE.value())))
      .where(
        commentCursorId(getCommentsReq.getCommentId()),
        retrieveCommentCondition(getCommentsReq.getFeedId())
      )
      .groupBy(comment.id)
      .orderBy(comment.id.desc())
      .limit(getCommentsReq.getSize())
      .fetch();
  }

  @Override
  public Long countReplyInfos(CommentDto.GetRepliesReq getRepliesReq) {
    return jpaQueryFactory
      .select(comment.count())
      .from(comment)
      .where(
        retrieveReplyCondition(getRepliesReq.getParentCommentId())
      )
      .fetchFirst();
  }

  @Override
  public List<CommentDto.ReplyRawInfo> findReplyInfos(CommentDto.GetRepliesReq getRepliesReq) {
    return jpaQueryFactory
      .select(Projections.bean(
        CommentDto.ReplyRawInfo.class,
        comment.id.as("commentId"),
        Expressions.as(Expressions.constant(getRepliesReq.getParentCommentId()), "parentCommentId"),
        comment.content,
        comment.createdAt,
        comment.updatedAt
      ))
      .from(comment)
      .where(
        replyCursorId(getRepliesReq.getCommentId()),
        retrieveReplyCondition(getRepliesReq.getParentCommentId())
      )
      .orderBy(comment.id.desc())
      .limit(getRepliesReq.getSize())
      .fetch();
  }

  private BooleanExpression commentCursorId(Long commentId) {
    return commentId != 0 ? comment.id.lt(commentId) : null;
  }

  private BooleanExpression replyCursorId(Long commentId) {
    return commentId != 0 ? comment.id.lt(commentId) : null;
  }

  /**
   * 1. 피드 아이디가 같은 것
   * 2. 부모 아이디가 존재하지 않는 것
   * 3. 삭제가 되지 않은 경우
   */
  private BooleanExpression retrieveCommentCondition(Long feedId) {
    return comment.feed.id.eq(feedId).and(comment.parent.isNull()).and(comment.status.eq(ACTIVE.value()));
  }

  /**
   * 1. 부모 댓글이 같은 것
   * 2. 삭제가 되지 않은 경우
   */
  private BooleanExpression retrieveReplyCondition(Long parentCommentId) {
    return comment.parent.id.eq(parentCommentId).and(comment.status.eq(ACTIVE.value()));
  }
}
