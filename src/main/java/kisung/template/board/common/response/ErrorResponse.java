package kisung.template.board.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kisung.template.board.common.code.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
  @Schema(description = "http status 코드")
  private final int status;
  @Schema(description = "내부 게시판 서비스 코드")
  private final String code;
  @Schema(description = "에러 메시지")
  private final String resultMsg;

  public ErrorResponse(ErrorCode code) {
    this.resultMsg = code.getMessage();
    this.status = code.getStatus();
    this.code = code.getCode();
  }

  public static ErrorResponse of(ErrorCode errorCode) {
    return new ErrorResponse(errorCode);
  }
}
