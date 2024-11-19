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
import kisung.template.board.dto.CommentDto;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static kisung.template.board.common.code.ErrorCode.*;
import static kisung.template.board.common.code.SuccessCode.*;

@RestController
@RequestMapping(value = "apis/v1/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
  private final CommentService commentService;

  @Operation(summary = "댓글 생성", description = "댓글 생성 서비스 입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Success"),
      @ApiResponse(responseCode = "AUTH_ERROR_001", description = "Token is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "AUTH_ERROR_002", description = "Authorize Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_011", description = "Not exist user",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "COMMENT_ERROR_001", description = "Content is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "COMMENT_ERROR_002", description = "Not exist comment",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "COMMENT_ERROR_003", description = "Not exist parent comment",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "COMMENT_ERROR_005", description = "Parent Comment Id is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "COMMENT_ERROR_004", description = "Comment content' length is under 300 characters",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_004", description = "Not exist feed",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping(value = "", produces = "application/json")
  @PreAuthorize("hasRole('USER')")
  public BasicResponse<CommentDto.PostCommetsRes> postComments(@RequestBody CommentDto.PostCommentsReq postCommentsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(commentService.createComments(postCommentsReq, userInfo), CREATE_SUCCESS);
  }

  @Operation(summary = "댓글 조회", description = "댓글 조회 서비스 입니다.")
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
    @ApiResponse(responseCode = "COMMENT_ERROR_006", description = "COMMENT Id is empty",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "FEED_ERROR_003", description = "Page Size is empty",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping(value = "", produces = "application/json")
  @PreAuthorize("hasRole('USER')")
  public BasicResponse<CommentDto.GetCommentsRes> getComments(CommentDto.GetCommentsReq getCommentsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(commentService.retrieveComments(getCommentsReq, userInfo), READ_SUCCESS);
  }

  @Operation(summary = "답글 조회", description = "답글 조회 서비스 입니다.")
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
      @ApiResponse(responseCode = "COMMENT_ERROR_006", description = "COMMENT Id is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "FEED_ERROR_003", description = "Page Size is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping(value = "replies", produces = "application/json")
  @PreAuthorize("hasRole('USER')")
  public BasicResponse<CommentDto.GetRepliesRes> getReplies(CommentDto.GetRepliesReq getRepliesReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(commentService.retrieveReplies(getRepliesReq, userInfo), READ_SUCCESS);
  }
}
