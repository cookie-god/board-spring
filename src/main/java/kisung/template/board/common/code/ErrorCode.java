package kisung.template.board.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  NON_EXIST_EMAIL(400, "BOARD_ERROR_001", "Email is empty"),
  NON_EXIST_PASSWORD(400, "BOARD_ERROR_002", "Password is empty"),
  NON_EXIST_NICKNAME(400, "BOARD_ERROR_003", "Nickname is empty"),
  INVALID_EMAIL(400, "BOARD_ERROR_004", "Email is invalid"),
  INVALID_PASSWORD(400, "BOARD_ERROR_005", "Password is invalid"),
  INVALID_NICKNAME(400, "BOARD_ERROR_006", "Nickname is invalid"),

  INTERNAL_SERVER_ERROR(500, "SERVER_ERROR_001", "Server Error");

  private final int status;
  private final String code;
  private final String message;
}
