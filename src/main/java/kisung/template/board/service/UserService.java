package kisung.template.board.service;

import kisung.template.board.dto.UserDto;

public interface UserService {
  UserDto.PostUsersRes createUser(UserDto.PostUserReq postUserReq);
  UserDto.PostLoginRes login(UserDto.PostLoginReq postLoginReq);
}
