package kisung.template.board.config.exception;

import kisung.template.board.common.code.ErrorCode;
import kisung.template.board.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

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

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex){
    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.USER_AUTHORIZE_ERROR);
    StackTraceElement[] stackTraceElements = ex.getStackTrace();
    log.error("error = {}", stackTraceElements[0]);
    return ResponseEntity
            .status(errorResponse.getStatus())
            .body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex){
    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
    StackTraceElement[] stackTraceElements = ex.getStackTrace();
    log.error("error = {}", stackTraceElements[0]);
    System.out.println("stackTraceElements = " + Arrays.toString(stackTraceElements));
    return ResponseEntity
        .status(errorResponse.getStatus())
        .body(errorResponse);
  }
}
