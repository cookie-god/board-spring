package kisung.template.board.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import kisung.template.board.common.response.BasicResponse;
import kisung.template.board.common.response.ErrorResponse;
import kisung.template.board.controller.user.swagger.PatchUserPasswordSwagger;
import kisung.template.board.controller.user.swagger.PostUsersLoginSwagger;
import kisung.template.board.controller.user.swagger.PostUsersSwagger;
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

  @PostUsersSwagger
  @PostMapping(value = "", produces = "application/json")
  public BasicResponse<UserDto.PostUsersRes> postUsers(@RequestBody UserDto.PostUserReq postUserReq) {
    return BasicResponse.success(userService.createUser(postUserReq), CREATE_SUCCESS);
  }

  @PostUsersLoginSwagger
  @PostMapping(value = "login", produces = "application/json")
  public BasicResponse<UserDto.PostLoginRes> postUserLogin(@RequestBody UserDto.PostLoginReq postLoginReq) {
    return BasicResponse.success(userService.login(postLoginReq), READ_SUCCESS);
  }

  @PatchUserPasswordSwagger
  @PatchMapping(value = "password", produces = "application/json")
  public BasicResponse<UserDto.PatchUserPasswordRes> patchUserPassword(@RequestBody UserDto.PatchUserPasswordReq patchUserPasswordReq) {
    return BasicResponse.success(userService.editUserPassword(patchUserPasswordReq), UPDATE_SUCCESS);
  }
}
