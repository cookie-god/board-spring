package kisung.template.board.service.bookmark;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kisung.template.board.entity.Feed;
import kisung.template.board.entity.FeedBookmark;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.enums.Role;
import kisung.template.board.repository.bookmark.feed.FeedBookmarkRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChangeFeedBookmarkStatusTest {
  @Mock
  private FeedBookmarkRepository feedBookmarkRepository;
  @InjectMocks
  private BookmarkServiceImpl bookmarkService;
  private JsonNode testData;

  @BeforeEach
  void setUp() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    testData = objectMapper.readTree(new File("src/test/resources/FeedServiceTestData.json"));
  }

  @Test
  @DisplayName("피드 즐겨찾기 상태 변경(해제) 성공")
  void changeFeedBookmark_success_1() {
    // given
    JsonNode data = testData.get("editFeedBookmarkReq").get("validData");
    UserInfo userInfo = makeUserInfoEntity();
    Feed feed = makeFeedEntity(userInfo);
    FeedBookmark feedBookmark = makeFeedBookmarkEntity(userInfo, feed);

    when(feedBookmarkRepository.findFeedBookmarkByUserIdAndFeedId(any(Long.class), any(Long.class))).thenReturn(Optional.of(feedBookmark));
    doNothing().when(feedBookmarkRepository).delete(any(FeedBookmark.class));

    // when
    boolean value = bookmarkService.changeFeedBookmark(userInfo, feed);

    // then
    assertFalse(value);
  }

  @Test
  @DisplayName("피드 즐겨찾기 상태 변경(등록) 성공")
  void changeFeedBookmark_success_2() {
    // given
    JsonNode data = testData.get("editFeedBookmarkReq").get("validData");
    UserInfo userInfo = makeUserInfoEntity();
    Feed feed = makeFeedEntity(userInfo);
    FeedBookmark feedBookmark = makeFeedBookmarkEntity(userInfo, feed);

    when(feedBookmarkRepository.findFeedBookmarkByUserIdAndFeedId(any(Long.class), any(Long.class))).thenReturn(Optional.empty());
    when(feedBookmarkRepository.save(any(FeedBookmark.class))).thenReturn(feedBookmark);

    // when
    boolean value = bookmarkService.changeFeedBookmark(userInfo, feed);

    // then
    assertTrue(value);
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
