package kisung.template.board.config.exception;

import kisung.template.board.common.code.ErrorCode;
import kisung.template.board.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(BoardException.class)
  public ResponseEntity<ErrorResponse> BoardExceptionHandler(BoardException exception) {
    ErrorResponse errorResponse = ErrorResponse.of(exception.getErrorCode());
    return ResponseEntity
        .status(errorResponse.getStatus())
        .body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex){
    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
    StackTraceElement[] stackTraceElements = ex.getStackTrace();
    log.error("error = {}", stackTraceElements[0]);
    return ResponseEntity
        .status(errorResponse.getStatus())
        .body(errorResponse);
  }
}
