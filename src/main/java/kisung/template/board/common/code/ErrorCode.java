package kisung.template.board.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  NON_EXIST_EMAIL(400, "BOARD_ERROR_001", "Email is empty"),
  NON_EXIST_PASSWORD(400, "BOARD_ERROR_002", "Password is empty"),
  NON_EXIST_NICKNAME(400, "BOARD_ERROR_003", "Nickane is empty"),

  INTERNAL_SERVER_ERROR(500, "SERVER_ERROR_001", "Server Error");

  private final int status;
  private final String code;
  private final String message;
}
