package kisung.template.board.service.auth;

import kisung.template.board.entity.UserInfo;

public interface AuthService {
  UserInfo retrieveUserInfoById(Long userId);
}
