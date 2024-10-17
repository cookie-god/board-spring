package kisung.template.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import kisung.template.board.common.code.SuccessCode;
import kisung.template.board.common.response.ApiResponse;
import kisung.template.board.dto.UserDto;
import kisung.template.board.service.UserService;
import kisung.template.board.service.UserServiceImpl;
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
  @PostMapping("")
  public ApiResponse<UserDto.PostUsersRes> postUsers(@RequestBody UserDto.PostUserReq postUserReq) {
    return ApiResponse.success(userService.createUser(postUserReq), CREATE_SUCCESS);
  }
}
