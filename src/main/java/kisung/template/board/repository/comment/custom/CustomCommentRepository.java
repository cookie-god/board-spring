package kisung.template.board.repository.comment.custom;

import kisung.template.board.entity.Comment;

import java.util.Optional;

public interface CustomCommentRepository {
  Optional<Comment> findCommentById(Long commentId);
}
