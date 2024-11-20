package kisung.template.board.repository.comment.custom;

import kisung.template.board.dto.CommentDto;
import kisung.template.board.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CustomCommentRepository {
  Optional<Comment> findCommentById(Long commentId);
  Long countCommentInfos(CommentDto.GetCommentsReq getCommentsReq);
  List<CommentDto.CommentRawInfo> findCommentInfos(CommentDto.GetCommentsReq getCommentsReq);
  Long countReplyInfos(CommentDto.GetRepliesReq getRepliesReq);
  List<CommentDto.ReplyRawInfo> findReplyInfos(CommentDto.GetRepliesReq getRepliesReq);
}
