package kisung.template.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import kisung.template.board.common.response.BasicResponse;
import kisung.template.board.common.response.ErrorResponse;
import kisung.template.board.dto.UserDto;
import kisung.template.board.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static kisung.template.board.common.code.SuccessCode.*;

@RestController
@RequestMapping(value = "/apis/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
  private final UserService userService;

  @Operation(summary = "유저 회원 가입", description = "유저 회원 가입 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success"),
      @ApiResponse(responseCode = "USER_ERROR_001", description = "Email is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_002", description = "Password is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_003", description = "Nickname is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_004", description = "Email is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_005", description = "Password is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_006", description = "Nickname is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_007", description = "Email is already exist",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_008", description = "Nickname is already exist",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "SERVER_ERROR_001", description = "Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping(value = "", produces = "application/json")
  public BasicResponse<UserDto.PostUsersRes> postUsers(@RequestBody UserDto.PostUserReq postUserReq) {
    return BasicResponse.success(userService.createUser(postUserReq), CREATE_SUCCESS);
  }

  @Operation(summary = "유저 로그인", description = "유저 로그인 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success"),
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
      @ApiResponse(responseCode = "SERVER_ERROR_001", description = "Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping(value = "login", produces = "application/json")
  public BasicResponse<UserDto.PostLoginRes> postUserLogin(@RequestBody UserDto.PostLoginReq postLoginReq) {
    return BasicResponse.success(userService.login(postLoginReq), READ_SUCCESS);
  }

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
  @PatchMapping(value = "password", produces = "application/json")
  public BasicResponse<UserDto.PatchUserPasswordRes> patchUserPassword(@RequestBody UserDto.PatchUserPasswordReq patchUserPasswordReq) {
    return BasicResponse.success(userService.editUserPassword(patchUserPasswordReq), UPDATE_SUCCESS);
  }
}
