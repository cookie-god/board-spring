package kisung.template.board.common.response;

import kisung.template.board.common.code.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
  private final int status;
  private final String code;
  private final String resultMsg;
  private String reason;

  public ErrorResponse(ErrorCode code) {
    this.resultMsg = code.getMessage();
    this.status = code.getStatus();
    this.code = code.getCode();
  }

  public ErrorResponse(ErrorCode code, String reason) {
    this.resultMsg = code.getMessage();
    this.status = code.getStatus();
    this.code = code.getCode();
    this.reason = reason;
  }

  public static ErrorResponse of(ErrorCode errorCode) {
    return new ErrorResponse(errorCode);
  }

  public static ErrorResponse of(ErrorCode errorCode, String reason) {
    return new ErrorResponse(errorCode, reason);
  }
}
