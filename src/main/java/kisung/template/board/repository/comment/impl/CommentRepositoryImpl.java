package kisung.template.board.repository.comment.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kisung.template.board.entity.Comment;
import kisung.template.board.repository.comment.custom.CustomCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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
}
