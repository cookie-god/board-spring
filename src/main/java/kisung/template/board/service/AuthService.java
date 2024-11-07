package kisung.template.board.service;

import kisung.template.board.entity.UserInfo;

public interface AuthService {
  UserInfo retrieveUserInfoById(Long userId);
}
