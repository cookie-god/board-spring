package kisung.template.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kisung.template.board.common.response.BasicResponse;
import kisung.template.board.dto.UserDto;
import kisung.template.board.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kisung.template.board.common.code.SuccessCode.CREATE_SUCCESS;

@RestController
@RequestMapping(value = "/apis/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
  private final UserService userService;

  @Operation(summary = "유저 회원가입", description = "유저 회원가입 입니다.")
  @ApiResponses({
    @ApiResponse(responseCode = "BOARD_ERROR_001", description = "Email is empty"),
    @ApiResponse(responseCode = "BOARD_ERROR_002", description = "Password is empty"),
    @ApiResponse(responseCode = "BOARD_ERROR_003", description = "Nickname is empty"),
    @ApiResponse(responseCode = "BOARD_ERROR_004", description = "Email is invalid"),
    @ApiResponse(responseCode = "BOARD_ERROR_005", description = "Password is invalid"),
    @ApiResponse(responseCode = "BOARD_ERROR_006", description = "Nickname is invalid"),
    @ApiResponse(responseCode = "SERVER_ERROR_001", description = "Server Error")
  })
  @PostMapping("")
  public BasicResponse<UserDto.PostUsersRes> postUsers(@RequestBody UserDto.PostUserReq postUserReq) {
    return BasicResponse.success(userService.createUser(postUserReq), CREATE_SUCCESS);
  }
}
