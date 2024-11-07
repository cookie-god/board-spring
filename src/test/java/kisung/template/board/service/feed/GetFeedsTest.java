package kisung.template.board.service.feed;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.FeedDto;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetFeedsTest {
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
  @DisplayName("피드 조회 (검색 O) 성공")
  void getFeedsWithSearch_success() {
    // given
    JsonNode data = testData.get("getFeedsReq").get("validRequestWithSearch");
    FeedDto.GetFeedsReq getFeedsReq = makeGetFeedsReq(data.get("feedId").asLong(), data.get("size").asInt(), data.get("searchKeyword").asText());
    List<FeedDto.FeedRawInfo> feedRawInfos = makeFeedRawInfoList(getFeedsReq.getSize());

    when(feedRepository.countFeedInfos(any(FeedDto.GetFeedsReq.class))).thenReturn(100L);
    when(feedRepository.findFeedInfos(any(FeedDto.GetFeedsReq.class))).thenReturn(feedRawInfos);

    // when
    FeedDto.GetFeedsRes getFeedsRes = feedService.retrieveFeeds(getFeedsReq);

    // then
    assertEquals(100L, getFeedsRes.getCount());
    assertEquals(getFeedsReq.getSize(), getFeedsRes.getFeeds().size());
  }

  @Test
  @DisplayName("피드 조회 (검색 X) 성공")
  void getFeedsWithoutSearch_success() {
    // given
    JsonNode data = testData.get("getFeedsReq").get("validRequestWithoutSearch");
    FeedDto.GetFeedsReq getFeedsReq = makeGetFeedsReq(data.get("feedId").asLong(), data.get("size").asInt(), null);
    List<FeedDto.FeedRawInfo> feedRawInfos = makeFeedRawInfoList(getFeedsReq.getSize());

    when(feedRepository.countFeedInfos(any(FeedDto.GetFeedsReq.class))).thenReturn(100L);
    when(feedRepository.findFeedInfos(any(FeedDto.GetFeedsReq.class))).thenReturn(feedRawInfos);

    // when
    FeedDto.GetFeedsRes getFeedsRes = feedService.retrieveFeeds(getFeedsReq);

    // then
    assertEquals(100L, getFeedsRes.getCount());
    assertEquals(getFeedsReq.getSize(), getFeedsRes.getFeeds().size());
  }

  @Test
  @DisplayName("피드 조회 실패 - 피드 아이디가 없는 경우 실패")
  void getFeeds_fail_empty_feed_id() {
    //given
    JsonNode data = testData.get("getFeedsReq").get("emptyFeedId");
    FeedDto.GetFeedsReq getFeedsReq = makeGetFeedsReq(null, data.get("size").asInt(), null);

    //then
    assertThrows(BoardException.class, () -> feedService.retrieveFeeds(getFeedsReq));
  }

  @Test
  @DisplayName("피드 조회 실패 - 페이지 사이즈가 없는 경우 실패")
  void getFeeds_fail_empty_page_size() {
    //given
    JsonNode data = testData.get("getFeedsReq").get("emptyPageSize");
    FeedDto.GetFeedsReq getFeedsReq = makeGetFeedsReq(data.get("feedId").asLong(), null, null);

    //then
    assertThrows(BoardException.class, () -> feedService.retrieveFeeds(getFeedsReq));
  }

  private FeedDto.GetFeedsReq makeGetFeedsReq(Long feedId, Integer size, String searchKeyword) {
    return FeedDto.GetFeedsReq.builder()
        .feedId(feedId)
        .size(size)
        .searchKeyword(searchKeyword)
        .build();
  }

  List<FeedDto.FeedRawInfo> makeFeedRawInfoList(int size) {
    List<FeedDto.FeedRawInfo> feedRawInfos = new ArrayList<>();
    for (long i = 1; i <= size; i++) {
      feedRawInfos.add(
          FeedDto.FeedRawInfo.builder()
              .id(i)
              .content("하이용")
              .userId(1L)
              .email("lion0193@gmail.com")
              .role(Role.USER.value())
              .commentCnt(0L)
              .bookmarkCnt(0L)
              .createdAt(LocalDateTime.now())
              .updatedAt(LocalDateTime.now())
              .build()
      );
    }
    return feedRawInfos;
  }
}
