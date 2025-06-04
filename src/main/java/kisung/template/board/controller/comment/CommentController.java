package kisung.template.board.controller.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kisung.template.board.common.response.BasicResponse;
import kisung.template.board.common.response.ErrorResponse;
import kisung.template.board.config.SecurityUtil;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.controller.comment.swagger.*;
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


  @GetCommentsSwagger
  @GetMapping(value = "", produces = "application/json")
  @PreAuthorize("hasRole('USER')")
  public BasicResponse<CommentDto.GetCommentsRes> getComments(CommentDto.GetCommentsReq getCommentsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(commentService.retrieveComments(getCommentsReq, userInfo), READ_SUCCESS);
  }

  @GetRepliesSwagger
  @GetMapping(value = "replies", produces = "application/json")
  @PreAuthorize("hasRole('USER')")
  public BasicResponse<CommentDto.GetRepliesRes> getReplies(CommentDto.GetRepliesReq getRepliesReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(commentService.retrieveReplies(getRepliesReq, userInfo), READ_SUCCESS);
  }


  @PostCommentsSwagger
  @PostMapping(value = "", produces = "application/json")
  @PreAuthorize("hasRole('USER')")
  public BasicResponse<CommentDto.PostCommentsRes> postComments(@RequestBody CommentDto.PostCommentsReq postCommentsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(commentService.createComments(postCommentsReq, userInfo), CREATE_SUCCESS);
  }

  @PutCommentsSwagger
  @PutMapping(value = "", produces = "application/json")
  @PreAuthorize("hasRole('USER')")
  public BasicResponse<CommentDto.PutCommentsRes> putComments(@RequestBody CommentDto.PutCommentsReq putCommentsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(commentService.editComments(putCommentsReq, userInfo), UPDATE_SUCCESS);
  }

  @DeleteCommentsSwagger
  @DeleteMapping(value = "", produces = "application/json")
  @PreAuthorize("hasRole('USER')")
  public BasicResponse<CommentDto.DeleteCommentsRes> deleteComments(@RequestBody CommentDto.DeleteCommentsReq deleteCommentsReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new BoardException(NON_EXIST_USER));
    return BasicResponse.success(commentService.deleteComments(deleteCommentsReq, userInfo), DELETE_SUCCESS);
  }
}
