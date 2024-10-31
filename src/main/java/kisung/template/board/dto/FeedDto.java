package kisung.template.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class FeedDto {

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class PostFeedsReq {
    @Schema(description = "내용", example = "오늘도 행복하세요ㅎㅎ")
    private String content;
  }

  @Getter
  @Builder
  public static class PostFeedsRes {
    @Schema(description = "피드 아이디", example = "1")
    private Long feedId;
  }
}
