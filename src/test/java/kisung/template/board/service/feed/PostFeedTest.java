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

import static kisung.template.board.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostFeedTest {
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
  @DisplayName("피드 작성 성공")
  void createFeed_success() {
    // given
    JsonNode data = testData.get("postFeedReq").get("validFeed");
    FeedDto.PostFeedsReq postFeedsReq = makePostFeedReq(data.get("content").asText());
    Feed feed = makeFeedEntity(postFeedsReq.getContent());
    UserInfo userInfo = makeUserInfoEntity();

    when(feedRepository.save(any(Feed.class))).thenReturn(feed);

    // when
    FeedDto.PostFeedsRes postFeedsRes = feedService.createFeeds(postFeedsReq, userInfo);

    // then
    assertEquals(1L, postFeedsRes.getFeedId());
  }

  @Test
  @DisplayName("피드 작성 실패 - 내용이 비어있는 경우 실패")
  void createFeed_fail_empty_content_1() {
    //given
    JsonNode data = testData.get("postFeedReq").get("emptyContent");
    FeedDto.PostFeedsReq postFeedsReq = makePostFeedReq(null);
    UserInfo userInfo = makeUserInfoEntity();

    //then
    assertThrows(BoardException.class, () -> feedService.createFeeds(postFeedsReq, userInfo));
  }

  @Test
  @DisplayName("피드 작성 실패 - 내용이 null인 경우")
  void createFeed_fail_empty_content_2() {
    //given
    FeedDto.PostFeedsReq postFeedsReq = makePostFeedReq(null);
    UserInfo userInfo = makeUserInfoEntity();

    //then
    assertThrows(BoardException.class, () -> feedService.createFeeds(postFeedsReq, userInfo));
  }

  private FeedDto.PostFeedsReq makePostFeedReq(String content) {
    return FeedDto.PostFeedsReq.builder()
      .content(content)
      .build();
  }

  private UserInfo makeUserInfoEntity() {
    LocalDateTime now = LocalDateTime.now();
    return UserInfo.builder()
      .id(1L)
      .email("lion0193@gmail.com")
      .nickname("쿠키")
      .password("99999999")
      .role(Role.USER.value())
      .createdAt(now)
      .updatedAt(now)
      .status(ACTIVE.value())
      .build();
  }

  private Feed makeFeedEntity(String content) {
    LocalDateTime now = LocalDateTime.now();
    return Feed.builder()
      .id(1L)
      .content(content)
      .commentCnt(0L)
      .bookmarkCnt(0L)
      .createdAt(now)
      .updatedAt(now)
      .status(ACTIVE.value())
      .build();
  }

}
