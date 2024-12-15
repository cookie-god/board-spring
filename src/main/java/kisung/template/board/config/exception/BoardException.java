package kisung.template.board.config.exception;

import kisung.template.board.common.code.ErrorCode;
import lombok.Getter;

@Getter
public class BoardException extends RuntimeException {
  private final ErrorCode errorCode;

  public BoardException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
