package kisung.template.board.controller.feed;

import kisung.template.board.common.response.BasicResponse;
import kisung.template.board.config.SecurityUtil;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.controller.feed.swagger.*;
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

  @PostFeedsSwagger
  @PreAuthorize("hasRole('USER')")
  @PostMapping(value = "", produces = "application/json")
  public BasicResponse<FeedDto.PostFeedsRes> postFeeds(@RequestBody FeedDto.PostFeedsReq postFeedsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(feedService.createFeeds(postFeedsReq, userInfo), CREATE_SUCCESS);
  }

  @GetFeedsSwagger
  @PreAuthorize("hasRole('USER')")
  @GetMapping(value = "", produces = "application/json")
  public BasicResponse<FeedDto.GetFeedsRes> getFeeds(@ParameterObject FeedDto.GetFeedsReq getFeedsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(feedService.retrieveFeeds(getFeedsReq), READ_SUCCESS);
  }

  @PutFeedsSwagger
  @PreAuthorize("hasRole('USER')")
  @PutMapping(value = "", produces = "application/json")
  public BasicResponse<FeedDto.PutFeedsRes> putFeeds(@RequestBody FeedDto.PutFeedsReq putFeedsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(feedService.editFeeds(putFeedsReq, userInfo), UPDATE_SUCCESS);
  }

  @DeleteFeedsSwagger
  @PreAuthorize("hasRole('USER')")
  @DeleteMapping(value = "", produces = "application/json")
  public BasicResponse<FeedDto.DeleteFeedsRes> deleteFeeds(@RequestBody FeedDto.DeleteFeedsReq deleteFeedsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(feedService.deleteFeeds(deleteFeedsReq, userInfo), DELETE_SUCCESS);
  }

  @GetFeedSwagger
  @PreAuthorize("hasRole('USER')")
  @GetMapping(value = "{feedId}", produces = "application/json")
  public BasicResponse<FeedDto.GetFeedRes> getFeed(@PathVariable("feedId") Long feedId) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(feedService.retrieveFeed(feedId, userInfo), READ_SUCCESS);
  }

  @PutBookmarkStatusSwagger
  @PreAuthorize("hasRole('USER')")
  @PutMapping(value = "bookmark/{feedId}/status", produces = "application/json")
  public BasicResponse<FeedDto.PutFeedBookmarkStatusRes> putFeedBookmark(@PathVariable("feedId") Long feedId) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(feedService.editFeedBookmarkStatus(feedId, userInfo), UPDATE_SUCCESS);
  }
}
