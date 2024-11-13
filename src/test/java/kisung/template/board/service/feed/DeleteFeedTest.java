package kisung.template.board.service.feed;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.FeedDto;
import kisung.template.board.entity.Feed;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.enums.Role;
import kisung.template.board.repository.feed.FeedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

import static kisung.template.board.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteFeedTest {
  @Mock
  private FeedRepository feedRepository;
  @InjectMocks
  private FeedServiceImpl feedService;
  private JsonNode testData;

  @BeforeEach
  void setUp() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    testData = objectMapper.readTree(new File("src/test/resources/FeedServiceTestData.json"));
  }

  @Test
  @DisplayName("피드 삭제 성공")
  void deleteFeeds_success() {
    // given
    JsonNode data = testData.get("deleteFeedReq").get("validFeed");
    FeedDto.DeleteFeedsReq deleteFeedsReq = makeDeleteFeedReq(data.get("feedId").asLong());
    UserInfo userInfo = makeUserInfoEntity(1L);
    Feed feed = makeFeedEntity(deleteFeedsReq.getFeedId(), userInfo);

    when(feedRepository.findFeedById(any(Long.class))).thenReturn(Optional.of(feed));

    // when
    FeedDto.DeleteFeedsRes deleteFeedsRes = feedService.deleteFeeds(deleteFeedsReq, userInfo);

    // then
    assertEquals(1L, deleteFeedsRes.getFeedId());
  }

  @Test
  @DisplayName("피드 삭제 실패 - 존재하지 않는 피드")
  void deleteFeeds_fail_not_exist_feed_1() {
    //given
    JsonNode data = testData.get("deleteFeedReq").get("notExistFeed");
    FeedDto.DeleteFeedsReq deleteFeedsReq = makeDeleteFeedReq(data.get("feedId").asLong());
    UserInfo userInfo = makeUserInfoEntity(1L);

    when(feedRepository.findFeedById(any(Long.class))).thenReturn(Optional.empty());

    //when, then
    assertThrows(BoardException.class, () -> feedService.deleteFeeds(deleteFeedsReq, userInfo));
  }

  @Test
  @DisplayName("피드 삭제 실패 - 내 게시물이 아닌 피드")
  void editFeeds_fail_not_my_feed_2() {
    //given
    JsonNode data = testData.get("deleteFeedReq").get("notMyFeed");
    FeedDto.DeleteFeedsReq deleteFeedsReq = makeDeleteFeedReq(data.get("feedId").asLong());
    UserInfo userInfo = makeUserInfoEntity(1L);
    UserInfo anotherUserInfo = makeUserInfoEntity(2L);
    Feed feed = makeFeedEntity(deleteFeedsReq.getFeedId(), anotherUserInfo);

    when(feedRepository.findFeedById(any(Long.class))).thenReturn(Optional.of(feed));

    //when, then
    assertThrows(BoardException.class, () -> feedService.deleteFeeds(deleteFeedsReq, userInfo));
  }

  @Test
  @DisplayName("피드 삭제 실패 - 피드 아이디가 존재하지 않는 경우")
  void editFeeds_fail_empty_content_3() {
    //given
    JsonNode data = testData.get("deleteFeedReq").get("emptyFeedId");
    FeedDto.DeleteFeedsReq deleteFeedsReq = makeDeleteFeedReq(null);
    UserInfo userInfo = makeUserInfoEntity(1L);

    //when, then
    assertThrows(BoardException.class, () -> feedService.deleteFeeds(deleteFeedsReq, userInfo));
  }

  private FeedDto.DeleteFeedsReq makeDeleteFeedReq(Long feedId) {
    return FeedDto.DeleteFeedsReq.builder()
        .feedId(feedId)
        .build();
  }

  private UserInfo makeUserInfoEntity(Long userId) {
    LocalDateTime now = LocalDateTime.now();
    return UserInfo.builder()
        .id(userId)
        .email("lion0193@gmail.com")
        .nickname("쿠키")
        .password("99999999")
        .role(Role.USER.value())
        .createdAt(now)
        .updatedAt(now)
        .status(ACTIVE.value())
        .build();
  }

  private Feed makeFeedEntity(Long feedId, UserInfo userInfo) {
    LocalDateTime now = LocalDateTime.now();
    return Feed.builder()
        .id(feedId)
        .content("내용")
        .commentCnt(0L)
        .bookmarkCnt(0L)
        .userInfo(userInfo)
        .createdAt(now)
        .updatedAt(now)
        .status(ACTIVE.value())
        .build();
  }
}
