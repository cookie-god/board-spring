package kisung.template.board.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
  READ_SUCCESS(200, "SUCCESS-001", "READ SUCCESS"),
  CREATE_SUCCESS(201, "SUCCESS-001", "CREATE SUCCESS"),
  UPDATE_SUCCESS(204, "SUCCESS-001", "UPDATE SUCCESS"),
  DELETE_SUCCESS(200, "SUCCESS-001", "DELETE SUCCESS");

  private final int status;
  private final String code;
  private final String message;
}
