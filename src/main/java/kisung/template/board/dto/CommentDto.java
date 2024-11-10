package kisung.template.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  public static class PostCommetsRes {
    @Schema(description = "댓글 아이디", example = "1", requiredMode = REQUIRED)
    private Long commentId;
  }
}
