package kisung.template.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FeedDto {

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class PostFeedsReq {
    @Schema(description = "내용", example = "오늘도 행복하세요ㅎㅎ")
    private String content;
  }

  @Data
  @Builder
  public static class PostFeedsRes {
    @Schema(description = "피드 아이디", example = "1")
    private Long feedId;
  }

  @Data
  @Builder
  public static class GetFeedsRes {
    @Schema(description = "피드 개수", example = "100")
    private int count;
    @Schema(description = "피드 리스트")
    private List<FeedInfo> feeds = new ArrayList<>();
  }

  @Data
  @Builder
  @AllArgsConstructor
  public static class FeedInfo {
    @Schema(description = "피드 아이디", example = "1")
    private Long feedId;
    @Schema(description = "내용", example = "오늘도 행복하세요ㅎㅎ")
    private String content;
    @Schema(description = "유저 기본 정보")
    private UserDto.UserBasicInfo userBasicInfo;
    @Schema(description = "댓글 수", example = "10")
    private Long commentCnt;
    @Schema(description = "좋아요 수", example = "10")
    private Long bookmarkCnt;
    @Schema(description = "작성 시간(초 단위)", example = "1722580995")
    private Long createdAt;
    @Schema(description = "수정 시간(초 단위)", example = "1722580995")
    private Long updatedAt;
  }

  @Data
  @NoArgsConstructor
  public static class FeedRawInfo {
    private Long id;
    private String content;
    private Long userId;
    private String email;
    private String nickname;
    private String role;
    private Long commentCnt;
    private Long bookmarkCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
  }
}
