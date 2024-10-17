package kisung.template.board.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kisung.template.board.common.code.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BasicResponse<T> {
  private T result;
  @Schema(description = "조회 및 삭제 성공은 200, 삽입 성공은 201, 수정은 204", example = "200")
  private int resultCode;
  @Schema(description = "성공 메시지", example = "READ SUCCESS")
  private String resultMsg;

  // API 성공 Response로 사용
  public static <T> BasicResponse<T> success(T result, SuccessCode successCode) {
    return new BasicResponse<T>(result, successCode.getStatus(), successCode.getMessage());
  }
}
