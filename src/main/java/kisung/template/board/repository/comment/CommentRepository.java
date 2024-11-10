package kisung.template.board.repository.comment;

import kisung.template.board.entity.Comment;
import kisung.template.board.repository.comment.custom.CustomCommentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {
}
