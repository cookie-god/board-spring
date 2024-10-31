package kisung.template.board.config;

import kisung.template.board.dto.UserDto;
import kisung.template.board.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class SecurityUtil {
  private SecurityUtil() {

  }

  public static Optional<Long> getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getPrincipal() == null) {
      throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
    }
    try {
      UserDto.UserBasicInfo userBasicInfo = (UserDto.UserBasicInfo) authentication.getPrincipal();
      return Optional.ofNullable(userBasicInfo.getUserId());
    } catch (Exception e) {
      System.out.println("e = " + e);
      return Optional.empty();
    }
  }
}
