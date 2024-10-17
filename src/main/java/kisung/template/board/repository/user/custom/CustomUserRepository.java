package kisung.template.board.repository.user.custom;

public interface CustomUserRepository {
  boolean existsByEmail(String email);
  boolean existsByNickname(String nickname);
}
