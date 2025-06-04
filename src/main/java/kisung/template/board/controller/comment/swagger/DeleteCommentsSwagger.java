package kisung.template.board.controller.comment.swagger;

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
@Operation(summary = "댓글 삭제", description = "댓글 삭제 서비스 입니다.")
@ApiResponses(value = {
  @ApiResponse(responseCode = "204", description = "Success"),
  @ApiResponse(responseCode = "AUTH_ERROR_001", description = "Token is invalid",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  @ApiResponse(responseCode = "AUTH_ERROR_002", description = "Authorize Error",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  @ApiResponse(responseCode = "USER_ERROR_011", description = "Not exist user",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  @ApiResponse(responseCode = "COMMENT_ERROR_006", description = "COMMENT Id is empty",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  @ApiResponse(responseCode = "COMMENT_ERROR_002", description = "Not exist comment",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface DeleteCommentsSwagger {
}
