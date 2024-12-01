package kisung.template.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public class FeedDto {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
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
  @AllArgsConstructor
  @NoArgsConstructor
  public static class GetFeedsReq {
    @Schema(description = "조회시 마지막 피드 아이디, 처음 조회라면 0 삽입", example = "0", requiredMode = REQUIRED)
    private Long feedId;
    @Schema(description = "페이징 사이즈", example = "10", requiredMode = REQUIRED)
    private Integer size;
    @Schema(description = "검색어", example = "행복", requiredMode = NOT_REQUIRED)
    private String searchKeyword;
  }

  @Data
  @Builder
  public static class GetFeedsRes {
    @Schema(description = "피드 총 개수", example = "100", requiredMode = REQUIRED)
    private Long count;
    @Schema(description = "피드 리스트", requiredMode = NOT_REQUIRED)
    private List<FeedInfo> feeds = new ArrayList<>();
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class PutFeedsReq {
    @Schema(description = "수정할 피드 아이디", example = "1", requiredMode = REQUIRED)
    private Long feedId;
    @Schema(description = "내용", example = "오늘도 행복하세요ㅎㅎ", requiredMode = REQUIRED)
    private String content;
  }

  @Data
  @Builder
  public static class PutFeedsRes {
    @Schema(description = "수정된 피드 아이디", example = "1", requiredMode = REQUIRED)
    private Long feedId;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DeleteFeedsReq {
    @Schema(description = "삭제할 피드 아이디", example = "1", requiredMode = REQUIRED)
    private Long feedId;
  }

  @Data
  @Builder
  public static class DeleteFeedsRes {
    @Schema(description = "삭제된 피드 아이디", example = "1", requiredMode = REQUIRED)
    private Long feedId;
  }

  @Data
  @Builder
  public static class PutFeedBookmarkStatusRes {
    @Schema(description = "좋아요 피드 아이디", example = "1", requiredMode = REQUIRED)
    private Long feedId;
    @Schema(description = "등록 or 해제 상태", example = "true", requiredMode = REQUIRED)
    private Boolean status;
  }

  @Data
  @Builder
  @AllArgsConstructor
  public static class FeedInfo {
    @Schema(description = "피드 아이디", example = "1", requiredMode = REQUIRED)
    private Long feedId;
    @Schema(description = "내용", example = "오늘도 행복하세요ㅎㅎ", requiredMode = REQUIRED)
    private String content;
    @Schema(description = "유저 기본 정보", requiredMode = REQUIRED)
    private UserDto.UserBasicInfo userBasicInfo;
    @Schema(description = "조회 수", example = "10", requiredMode = REQUIRED)
    private Long viewCnt;
    @Schema(description = "댓글 수", example = "10", requiredMode = REQUIRED)
    private Long commentCnt;
    @Schema(description = "좋아요 수", example = "10", requiredMode = REQUIRED)
    private Long bookmarkCnt;
    @Schema(description = "작성 시간(초 단위)", example = "1722580995", requiredMode = REQUIRED)
    private Long createdAt;
    @Schema(description = "수정 시간(초 단위)", example = "1722580995", requiredMode = REQUIRED)
    private Long updatedAt;
  }

  @Data
  @Builder
  @AllArgsConstructor
  public static class GetFeedRes {
    @Schema(description = "피드 아이디", example = "1", requiredMode = REQUIRED)
    private Long feedId;
    @Schema(description = "내용", example = "오늘도 행복하세요ㅎㅎ", requiredMode = REQUIRED)
    private String content;
    @Schema(description = "유저 기본 정보", requiredMode = REQUIRED)
    private UserDto.UserBasicInfo userBasicInfo;
    @Schema(description = "조회 수", example = "10", requiredMode = REQUIRED)
    private Long viewCnt;
    @Schema(description = "댓글 수", example = "10", requiredMode = REQUIRED)
    private Long commentCnt;
    @Schema(description = "좋아요 수", example = "10", requiredMode = REQUIRED)
    private Long bookmarkCnt;
    @Schema(description = "작성 시간(초 단위)", example = "1722580995", requiredMode = REQUIRED)
    private Long createdAt;
    @Schema(description = "수정 시간(초 단위)", example = "1722580995", requiredMode = REQUIRED)
    private Long updatedAt;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class FeedRawInfo {
    private Long id;
    private String content;
    private Long userId;
    private String email;
    private String nickname;
    private String role;
    private Long viewCnt;
    private Long commentCnt;
    private Long bookmarkCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
  }
}
