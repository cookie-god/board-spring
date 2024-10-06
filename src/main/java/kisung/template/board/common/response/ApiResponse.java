package kisung.template.board.common.response;

import kisung.template.board.common.code.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
  private T result;
  private int resultCode;
  private String resultMsg;

  // API 성공 Response로 사용
  public static <T> ApiResponse<T> success(T result, SuccessCode successCode) {
    return new ApiResponse<T>(result, successCode.getStatus(), successCode.getMessage());
  }
}
