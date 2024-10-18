package kisung.template.board.service;

import kisung.template.board.config.jwt.CustomPrincipal;
import kisung.template.board.dto.UserDto;
import kisung.template.board.entity.UserInfo;

public interface UserService {
  UserDto.PostUsersRes createUser(UserDto.PostUserReq postUserReq);
  UserDto.PostUserLoginRes login(UserDto.PostUserLoginReq postUserLoginReq);
}
