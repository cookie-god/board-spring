package kisung.template.board.config;

import kisung.template.board.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class SecurityUtil {
  private SecurityUtil() {

  }

  public static Optional<UserInfo> getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getPrincipal() == null) {
      throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
    }
    try {
      UserInfo userInfo = (UserInfo) authentication.getPrincipal();
      return Optional.ofNullable(userInfo);
    } catch (Exception e) {
      log.error("error = {}", e);
      return Optional.empty();
    }
  }
}
