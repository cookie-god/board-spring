package kisung.template.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public class CommentDto {
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PostCommentsReq {
    @Schema(description = "피드 아이디", example = "1", requiredMode = REQUIRED)
    private Long feedId;
    @Schema(description = "부모 댓글 아이디(없다면 0 입력)", example = "0", requiredMode = REQUIRED)
    private Long parentCommentId;
    @Schema(description = "댓글 내용", example = "당신은 행복하셨나요?", requiredMode = REQUIRED)
    private String content;
  }

  @Data
  @Builder
  public static class PostCommentsRes {
    @Schema(description = "댓글 아이디", example = "1", requiredMode = REQUIRED)
    private Long commentId;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetCommentsReq {
    @Schema(description = "조회시 마지막 댓글 아이디, 처음 조회라면 0 삽입", example = "0", requiredMode = REQUIRED)
    private Long commentId;
    @Schema(description = "피드 아이디", example = "1", requiredMode = REQUIRED)
    private Long feedId;
    @Schema(description = "페이징 사이즈", example = "10", requiredMode = REQUIRED)
    private Integer size;
  }

  @Data
  @Builder
  public static class GetCommentsRes {
    @Schema(description = "댓글 총 개수", example = "100", requiredMode = REQUIRED)
    private Long count;
    @Schema(description = "댓글 리스트", requiredMode = NOT_REQUIRED)
    private List<CommentInfo> commentInfos = new ArrayList<>();
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetRepliesReq {
    @Schema(description = "댓글(하위) 아이디, 처음 조회라면 0 삽입", example = "0", requiredMode = REQUIRED)
    private Long commentId;
    @Schema(description = "댓글(상위) 아이디", example = "1", requiredMode = REQUIRED)
    private Long parentCommentId;
    @Schema(description = "페이징 사이즈", example = "10", requiredMode = REQUIRED)
    private Integer size;
  }

  @Data
  @Builder
  public static class GetRepliesRes {
    @Schema(description = "답글 총 개수", example = "100", requiredMode = REQUIRED)
    private Long count;
    @Schema(description = "답글 리스트", requiredMode = NOT_REQUIRED)
    private List<ReplyInfo> replyInfos = new ArrayList<>();
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PutCommentsReq {
    @Schema(description = "댓글", example = "1", requiredMode = REQUIRED)
    private Long commentId;
    @Schema(description = "댓글 내용", example = "당신은 행복하셨나요?", requiredMode = REQUIRED)
    private String content;
  }

  @Data
  @Builder
  public static class PutCommentsRes {
    @Schema(description = "댓글 아이디", example = "1", requiredMode = REQUIRED)
    private Long commentId;
  }


  @Data
  @Builder
  @AllArgsConstructor
  public static class CommentInfo {
    @Schema(description = "댓글 아이디", example = "1", requiredMode = REQUIRED)
    private Long commentId;
    @Schema(description = "자식 댓글 개수", example = "1", requiredMode = REQUIRED)
    private Long childCommentCnt;
    @Schema(description = "댓글 내용", example = "당신은 행복하셨나요?", requiredMode = REQUIRED)
    private String content;
    @Schema(description = "작성 시간(초 단위)", example = "1722580995", requiredMode = REQUIRED)
    private Long createdAt;
    @Schema(description = "수정 시간(초 단위)", example = "1722580995", requiredMode = REQUIRED)
    private Long updatedAt;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CommentRawInfo {
    private Long commentId;
    private Long childCommentCnt;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
  }

  @Data
  @Builder
  @AllArgsConstructor
  public static class ReplyInfo {
    @Schema(description = "댓글(하위) 아이디", example = "1", requiredMode = REQUIRED)
    private Long commentId;
    @Schema(description = "댓글(상위) 아이디", example = "1", requiredMode = REQUIRED)
    private Long parentCommentId;
    @Schema(description = "댓글 내용", example = "당신은 행복하셨나요?", requiredMode = REQUIRED)
    private String content;
    @Schema(description = "작성 시간(초 단위)", example = "1722580995", requiredMode = REQUIRED)
    private Long createdAt;
    @Schema(description = "수정 시간(초 단위)", example = "1722580995", requiredMode = REQUIRED)
    private Long updatedAt;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ReplyRawInfo {
    private Long commentId;
    private Long parentCommentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
  }
}
