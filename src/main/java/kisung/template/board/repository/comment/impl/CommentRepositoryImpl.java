package kisung.template.board.repository.comment.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
          retrieveCommentCondition(getCommentsReq.getFeedId()),
        cursorId(getCommentsReq.getCommentId())
      )
      .groupBy(comment.id)
      .orderBy(comment.id.desc())
      .limit(getCommentsReq.getSize())
      .fetch();
  }

  private BooleanExpression cursorId(Long commentId) {
    return commentId != 0 ? comment.id.lt(commentId) : null;
  }

  private BooleanExpression retrieveCommentCondition(Long feedId) {
    return comment.feed.id.eq(feedId).and(comment.parent.isNull()).and(comment.status.eq(ACTIVE.value()));
  }
}
