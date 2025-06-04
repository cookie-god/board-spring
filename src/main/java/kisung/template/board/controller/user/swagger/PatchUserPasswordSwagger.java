package kisung.template.board.controller.user.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kisung.template.board.common.response.ErrorResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "유저 비밀 번호 변경", description = "유저 비밀 번호 변경 입니다.")
@ApiResponses(value = {
  @ApiResponse(responseCode = "204", description = "Success"),
  @ApiResponse(responseCode = "USER_ERROR_001", description = "Email is empty",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  @ApiResponse(responseCode = "USER_ERROR_002", description = "Password is empty",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  @ApiResponse(responseCode = "USER_ERROR_004", description = "Email is invalid",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  @ApiResponse(responseCode = "USER_ERROR_005", description = "Password is invalid",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  @ApiResponse(responseCode = "USER_ERROR_010", description = "User's password is wrong.",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  @ApiResponse(responseCode = "USER_ERROR_012", description = "New Password is empty",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  @ApiResponse(responseCode = "USER_ERROR_013", description = "New Password is invalid",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  @ApiResponse(responseCode = "SERVER_ERROR_001", description = "Server Error",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface PatchUserPasswordSwagger {
}
