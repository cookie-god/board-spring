package kisung.template.board.service.feed;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.FeedDto;
import kisung.template.board.entity.Feed;
import kisung.template.board.entity.FeedBookmark;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.enums.Role;
import kisung.template.board.repository.bookmark.feed.FeedBookmarkRepository;
import kisung.template.board.repository.feed.FeedRepository;
import kisung.template.board.service.bookmark.BookmarkServiceImpl;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PutFeedBookmarkStatusTest {
  @Mock
  private BookmarkServiceImpl bookmarkService;
  @Mock
  private FeedBookmarkRepository feedBookmarkRepository;
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
  @DisplayName("피드 즐겨찾기 상태 변경 성공")
  void editFeedBookmarkStatus_success() {
    // given
    JsonNode data = testData.get("editFeedBookmarkReq").get("validData");
    Long feedId = data.get("feedId").asLong();
    UserInfo userInfo = makeUserInfoEntity();
    Feed feed = makeFeedEntity(userInfo);
//    FeedBookmark feedBookmark = makeFeedBookmarkEntity(userInfo, feed);

    when(feedRepository.findFeedById(any(Long.class))).thenReturn(Optional.of(feed));
    when(bookmarkService.changeFeedBookmark(any(UserInfo.class), any(Feed.class))).thenReturn(false);
//    when(feedBookmarkRepository.findFeedBookmarkByUserIdAndFeedId(any(Long.class), any(Long.class))).thenReturn(Optional.of(feedBookmark));
//    doNothing().when(feedBookmarkRepository).delete(any(FeedBookmark.class));

    // when
    FeedDto.PutFeedBookmarkStatusRes putFeedBookmarkStatusRes = feedService.editFeedBookmarkStatus(feedId, userInfo);

    // then
    assertEquals(false, putFeedBookmarkStatusRes.getStatus());
  }

  @Test
  @DisplayName("피드 즐겨찾기 상태 변경 실패 - 존재 하지 않는 피드")
  void editFeedBookmarkStatus_fail_not_exist_feed() {
    // given
    JsonNode data = testData.get("editFeedBookmarkReq").get("validData");
    Long feedId = data.get("feedId").asLong();
    UserInfo userInfo = makeUserInfoEntity();

    when(feedRepository.findFeedById(any(Long.class))).thenReturn(Optional.empty());

    //when, then
    assertThrows(BoardException.class, () -> feedService.editFeedBookmarkStatus(feedId, userInfo));
  }

  @Test
  @DisplayName("피드 즐겨찾기 상태 변경 실패 - 피드 아이디가 존재 하지 않는 경우")
  void editFeedBookmarkStatus_fail_empty_content() {
    //given
    JsonNode data = testData.get("editFeedBookmarkReq").get("emptyFeedId");
    Long feedId = data.get("feedId").asLong();
    UserInfo userInfo = makeUserInfoEntity();

    //when, then
    assertThrows(BoardException.class, () -> feedService.editFeedBookmarkStatus(feedId, userInfo));
  }

  private FeedBookmark makeFeedBookmarkEntity(UserInfo userInfo, Feed feed) {
    LocalDateTime now = LocalDateTime.now();
    return FeedBookmark.builder()
        .id(1L)
        .userInfo(userInfo)
        .feed(feed)
        .createdAt(now)
        .updatedAt(now)
        .status(ACTIVE.value())
        .build();
  }

  private Feed makeFeedEntity(UserInfo userInfo) {
    LocalDateTime now = LocalDateTime.now();
    return Feed.builder()
        .id(1L)
        .userInfo(userInfo)
        .content("테스트")
        .viewCnt(0L)
        .commentCnt(0L)
        .bookmarkCnt(0L)
        .createdAt(now)
        .updatedAt(now)
        .status(ACTIVE.value())
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
}
