package kisung.template.board.service.auth;

import kisung.template.board.entity.UserInfo;
import kisung.template.board.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;

  @Override
  public UserInfo retrieveUserInfoById(Long userId) {
    return userRepository.findUserInfoById(userId).orElse(null);
  }
}
