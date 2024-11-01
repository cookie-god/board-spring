package kisung.template.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kisung.template.board.common.code.ErrorCode;
import kisung.template.board.common.code.SuccessCode;
import kisung.template.board.common.response.BasicResponse;
import kisung.template.board.common.response.ErrorResponse;
import kisung.template.board.config.SecurityUtil;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.FeedDto;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/apis/v1/feeds")
@RequiredArgsConstructor
@Slf4j
public class FeedController {
  private final FeedService feedService;

  @Operation(summary = "피드 생성", description = "피드 조회 서비스 입니다.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Success"),
    @ApiResponse(responseCode = "AUTH_ERROR_001", description = "Token is invalid",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "AUTH_ERROR_002", description = "Authorize Error",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "USER_ERROR_011", description = "Not exist user",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "FEED_ERROR_001", description = "Content is empty",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  })
  @PreAuthorize("hasRole('USER')")
  @PostMapping(value = "")
  public BasicResponse<FeedDto.PostFeedsRes> postFeeds(@RequestBody FeedDto.PostFeedsReq postFeedsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(ErrorCode.NON_EXIST_USER));
    return BasicResponse.success(feedService.createFeed(postFeedsReq, userInfo), SuccessCode.CREATE_SUCCESS);
  }


}
