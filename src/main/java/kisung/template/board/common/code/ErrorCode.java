package kisung.template.board.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  NON_EXIST_EMAIL(400, "USER_ERROR_001", "Email is empty"),
  NON_EXIST_PASSWORD(400, "USER_ERROR_002", "Password is empty"),
  NON_EXIST_NICKNAME(400, "USER_ERROR_003", "Nickname is empty"),
  INVALID_EMAIL(400, "USER_ERROR_004", "Email is invalid"),
  INVALID_PASSWORD(400, "USER_ERROR_005", "Password is invalid"),
  INVALID_NICKNAME(400, "USER_ERROR_006", "Nickname is invalid"),
  DUPLICATE_EMAIL(404, "USER_ERROR_007", "email is already exist"),
  DUPLICATE_NICKNAME(404, "USER_ERROR_008", "Nickname is already exist"),
  NOT_EXIST_USER_BY_EMAIL(404, "USER_ERROR_009", "User's email is not exist."),
  WRONG_PASSWORD(404, "USER_ERROR_010", "User's password is wrong."),
  NON_EXIST_USER(400, "USER_ERROR_011", "Not exist user"),

  INTERNAL_SERVER_ERROR(500, "SERVER_ERROR_001", "Server Error");

  private final int status;
  private final String code;
  private final String message;
}
