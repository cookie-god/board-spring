package kisung.template.board.service.comment;

import kisung.template.board.dto.CommentDto;
import kisung.template.board.entity.UserInfo;

public interface CommentService {
  CommentDto.PostCommetsRes createComments(CommentDto.PostCommentsReq postCommentsReq, UserInfo userInfo);
}
