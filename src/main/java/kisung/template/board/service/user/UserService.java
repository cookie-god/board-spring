package kisung.template.board.service.user;

import kisung.template.board.dto.UserDto;

public interface UserService {
  UserDto.PostUsersRes createUser(UserDto.PostUserReq postUserReq);

  UserDto.PostLoginRes login(UserDto.PostLoginReq postLoginReq);

  UserDto.PatchUserPasswordRes editUserPassword(UserDto.PatchUserPasswordReq patchUserPasswordReq);
}
