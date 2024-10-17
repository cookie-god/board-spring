package kisung.template.board.repository.user.custom;

import kisung.template.board.entity.UserInfo;

import java.util.Optional;

public interface CustomUserRepository {
  boolean existsByEmail(String email);
  boolean existsByNickname(String nickname);
  Optional<UserInfo> findByEmail(String email);
}
