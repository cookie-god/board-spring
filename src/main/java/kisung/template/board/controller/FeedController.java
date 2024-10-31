package kisung.template.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import kisung.template.board.common.code.ErrorCode;
import kisung.template.board.common.code.SuccessCode;
import kisung.template.board.common.response.BasicResponse;
import kisung.template.board.config.SecurityUtil;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.FeedDto;
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
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping(value = "")
  public BasicResponse<FeedDto.PostFeedsRes> postFeeds(@RequestBody FeedDto.PostFeedsReq postFeedsReq) {
    Long userId = SecurityUtil.getUserId().orElseThrow(() -> new BoardException(ErrorCode.NON_EXIST_USER));
    return BasicResponse.success(feedService.createFeed(postFeedsReq, userId), SuccessCode.CREATE_SUCCESS);
  }


}
