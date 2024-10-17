package kisung.template.board.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserDto {

  @Getter
  public static class PostUserReq {
    @Schema(description = "이메일", example = "lion0193@gmail.com")
    private String email;
    @Schema(description = "비밀번호", example = "qwer1234!")
    private String password;
    @Schema(description = "닉네임", example = "쿠키갓")
    private String nickname;
  }

  @Getter
  @Builder
  public static class PostUsersRes {
    @Schema(description = "유저 아이디", example = "1")
    private Long userId;
  }
}
