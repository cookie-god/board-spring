package kisung.template.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.regex.Pattern;

public class UserDto {
  private static final String EMAIL_PATTERN = "^[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*$";
  private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$";

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class PostUserReq {
    @Schema(description = "이메일", example = "lion0193@gmail.com")
    private String email;
    @Schema(description = "비밀번호 (영문 숫자 특수기호 조합 8자리 이상)", example = "qwer1234!")
    private String password;
    @Schema(description = "닉네임", example = "쿠키갓")
    private String nickname;

    public boolean isEmail() {
      return Pattern.matches(EMAIL_PATTERN, email);
    }
    public boolean isPassword() {
      return Pattern.matches(PASSWORD_PATTERN, password);
    }
    public boolean isNickname() {
      return nickname.length() <= 10;
    }
  }

  @Getter
  @Builder
  public static class PostUsersRes {
    @Schema(description = "유저 아이디", example = "1")
    private Long userId;
  }

  @Getter
  public static class PostUserLoginReq {
    @Schema(description = "이메일", example = "lion0193@gmail.com")
    private String email;
    @Schema(description = "비밀번호 (영문 숫자 특수기호 조합 8자리 이상)", example = "qwer1234!")
    private String password;

    public boolean isEmail() {
      return Pattern.matches(EMAIL_PATTERN, email);
    }
    public boolean isPassword() {
      return Pattern.matches(PASSWORD_PATTERN, password);
    }
  }

  @Getter
  @Builder
  public static class PostUserLoginRes {
    @Schema(description = "jwt 토큰", example = "asdasdasd")
    private String token;
    @Schema(description = "유저 아이디", example = "1")
    private Long userId;
    @Schema(description = "이메일", example = "lion0193@gmail.com")
    private String email;
    @Schema(description = "닉네임", example = "쿠키갓")
    private String nickname;
    @Schema(description = "권한", example = "ROLE_USER")
    private String role;
  }

  @Getter
  @Builder
  public static class UserBasicInfo {
    @Schema(description = "유저 아이디", example = "1")
    private Long userId;
    @Schema(description = "이메일", example = "lion0193@gmail.com")
    private String email;
    @Schema(description = "닉네임", example = "쿠키갓")
    private String nickname;
    @Schema(description = "권한", example = "ROLE_USER")
    private String role;
  }
}
