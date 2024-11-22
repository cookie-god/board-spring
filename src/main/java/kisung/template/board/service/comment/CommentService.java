package kisung.template.board.service.comment;

import kisung.template.board.dto.CommentDto;
import kisung.template.board.entity.UserInfo;

public interface CommentService {
  CommentDto.PostCommentsRes createComments(CommentDto.PostCommentsReq postCommentsReq, UserInfo userInfo);
  CommentDto.GetCommentsRes retrieveComments(CommentDto.GetCommentsReq getCommentsReq, UserInfo userInfo);
  CommentDto.GetRepliesRes retrieveReplies(CommentDto.GetRepliesReq getRepliesReq, UserInfo userInfo);
}
