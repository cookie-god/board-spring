package kisung.template.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kisung.template.board.common.response.BasicResponse;
import kisung.template.board.common.response.ErrorResponse;
import kisung.template.board.config.SecurityUtil;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.FeedDto;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.service.feed.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static kisung.template.board.common.code.ErrorCode.*;
import static kisung.template.board.common.code.SuccessCode.*;

@RestController
@RequestMapping(value = "/apis/v1/feeds")
@RequiredArgsConstructor
@Slf4j
public class FeedController {
  private final FeedService feedService;

  @Operation(summary = "피드 생성", description = "피드 작성 서비스 입니다.")
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
  @PostMapping(value = "", produces = "application/json")
  public BasicResponse<FeedDto.PostFeedsRes> postFeeds(@RequestBody FeedDto.PostFeedsReq postFeedsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(feedService.createFeeds(postFeedsReq, userInfo), CREATE_SUCCESS);
  }

  @Operation(summary = "피드 조회 및 검색", description = "피드 조회 및 검색 서비스 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success"),
      @ApiResponse(responseCode = "AUTH_ERROR_001", description = "Token is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "AUTH_ERROR_002", description = "Authorize Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_011", description = "Not exist user",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_002", description = "Feed Id is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_003", description = "Page Size is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PreAuthorize("hasRole('USER')")
  @GetMapping(value = "", produces = "application/json")
  public BasicResponse<FeedDto.GetFeedsRes> getFeeds(@ParameterObject FeedDto.GetFeedsReq getFeedsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(feedService.retrieveFeeds(getFeedsReq), READ_SUCCESS);
  }

  @Operation(summary = "피드 수정", description = "피드 수정 서비스 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Success"),
      @ApiResponse(responseCode = "AUTH_ERROR_001", description = "Token is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "AUTH_ERROR_002", description = "Authorize Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_011", description = "Not exist user",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_001", description = "Content is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_002", description = "Feed Id is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_004", description = "Not exist feed",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_005", description = "Not my feed",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PreAuthorize("hasRole('USER')")
  @PutMapping(value = "", produces = "application/json")
  public BasicResponse<FeedDto.PutFeedsRes> putFeeds(@RequestBody FeedDto.PutFeedsReq putFeedsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(feedService.editFeeds(putFeedsReq, userInfo), UPDATE_SUCCESS);
  }

  @Operation(summary = "피드 삭제", description = "피드 삭제 서비스 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Success"),
      @ApiResponse(responseCode = "AUTH_ERROR_001", description = "Token is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "AUTH_ERROR_002", description = "Authorize Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_011", description = "Not exist user",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_002", description = "Feed Id is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_004", description = "Not exist feed",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_005", description = "Not my feed",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PreAuthorize("hasRole('USER')")
  @DeleteMapping(value = "", produces = "application/json")
  public BasicResponse<FeedDto.DeleteFeedsRes> deleteFeeds(@RequestBody FeedDto.DeleteFeedsReq deleteFeedsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(feedService.deleteFeeds(deleteFeedsReq, userInfo), DELETE_SUCCESS);
  }

  @Operation(summary = "피드 상세 조회", description = "피드 상세 조회 서비스 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success"),
      @ApiResponse(responseCode = "AUTH_ERROR_001", description = "Token is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "AUTH_ERROR_002", description = "Authorize Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_011", description = "Not exist user",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_002", description = "Feed Id is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_004", description = "Not exist feed",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PreAuthorize("hasRole('USER')")
  @GetMapping(value = "{feedId}", produces = "application/json")
  public BasicResponse<FeedDto.GetFeedRes> getFeed(@PathVariable("feedId") Long feedId) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(feedService.retrieveFeed(feedId, userInfo), READ_SUCCESS);
  }

  @Operation(summary = "피드 북마크 상태 변경", description = "피드 북마크 상태 변경 서비스 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success"),
      @ApiResponse(responseCode = "AUTH_ERROR_001", description = "Token is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "AUTH_ERROR_002", description = "Authorize Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_011", description = "Not exist user",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_002", description = "Feed Id is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_004", description = "Not exist feed",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PreAuthorize("hasRole('USER')")
  @PutMapping(value = "bookmark/{feedId}/status", produces = "application/json")
  public BasicResponse<FeedDto.PutFeedBookmarkStatusRes> putFeedBookmark(@PathVariable("feedId") Long feedId) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(feedService.editFeedBookmarkStatus(feedId, userInfo), UPDATE_SUCCESS);
  }
}
