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
public class PutFeedTest {
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
    @DisplayName("피드 수정 성공")
    void editFeeds_success() {
        // given
        JsonNode data = testData.get("putFeedReq").get("validFeed");
        FeedDto.PutFeedsReq putFeedsReq = makePutFeedReq(data.get("feedId").asLong(), data.get("content").asText());
        UserInfo userInfo = makeUserInfoEntity(1L);
        Feed feed = makeFeedEntity(putFeedsReq.getFeedId(), putFeedsReq.getContent(), userInfo);

        when(feedRepository.findFeedById(any(Long.class))).thenReturn(Optional.of(feed));

        // when
        FeedDto.PutFeedsRes putFeedsRes = feedService.editFeeds(putFeedsReq, userInfo);

        // then
        assertEquals(1L, putFeedsRes.getFeedId());
    }

    @Test
    @DisplayName("피드 수정 실패 - 존재하지 않는 피드")
    void editFeeds_fail_not_exist_feed_1() {
        //given
        JsonNode data = testData.get("putFeedReq").get("notExistFeed");
        FeedDto.PutFeedsReq putFeedsReq = makePutFeedReq(data.get("feedId").asLong(), data.get("content").asText());
        UserInfo userInfo = makeUserInfoEntity(1L);
        Feed feed = makeFeedEntity(putFeedsReq.getFeedId(), putFeedsReq.getContent(), userInfo);

        when(feedRepository.findFeedById(any(Long.class))).thenReturn(Optional.empty());

        //then
        assertThrows(BoardException.class, () -> feedService.editFeeds(putFeedsReq, userInfo));
    }

    @Test
    @DisplayName("피드 수정 실패 - 내 게시물이 아닌 피드")
    void editFeeds_fail_not_my_feed_2() {
        //given
        JsonNode data = testData.get("putFeedReq").get("notMyFeed");
        FeedDto.PutFeedsReq putFeedsReq = makePutFeedReq(data.get("feedId").asLong(), data.get("content").asText());
        UserInfo userInfo = makeUserInfoEntity(1L);
        UserInfo anotherUserInfo = makeUserInfoEntity(2L);
        Feed feed = makeFeedEntity(putFeedsReq.getFeedId(), putFeedsReq.getContent(), anotherUserInfo);

        when(feedRepository.findFeedById(any(Long.class))).thenReturn(Optional.of(feed));

        //then
        assertThrows(BoardException.class, () -> feedService.editFeeds(putFeedsReq, userInfo));
    }

    @Test
    @DisplayName("피드 수정 실패 - 내용이 빈 경우")
    void editFeeds_fail_empty_content_3() {
        //given
        JsonNode data = testData.get("putFeedReq").get("emptyContent");
        FeedDto.PutFeedsReq putFeedsReq = makePutFeedReq(data.get("feedId").asLong(), data.get("content").asText());
        UserInfo userInfo = makeUserInfoEntity(1L);

        //then
        assertThrows(BoardException.class, () -> feedService.editFeeds(putFeedsReq, userInfo));
    }

    @Test
    @DisplayName("피드 수정 실패 - 내용이 null인 경우")
    void editFeeds_fail_empty_content_4() {
        //given
        JsonNode data = testData.get("putFeedReq").get("emptyContent");
        FeedDto.PutFeedsReq putFeedsReq = makePutFeedReq(data.get("feedId").asLong(), null);
        UserInfo userInfo = makeUserInfoEntity(1L);

        //then
        assertThrows(BoardException.class, () -> feedService.editFeeds(putFeedsReq, userInfo));
    }

    private FeedDto.PutFeedsReq makePutFeedReq(Long feedId, String content) {
        return FeedDto.PutFeedsReq.builder()
                .feedId(feedId)
                .content(content)
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

    private Feed makeFeedEntity(Long feedId, String content, UserInfo userInfo) {
        LocalDateTime now = LocalDateTime.now();
        return Feed.builder()
                .id(feedId)
                .content(content)
                .commentCnt(0L)
                .bookmarkCnt(0L)
                .userInfo(userInfo)
                .createdAt(now)
                .updatedAt(now)
                .status(ACTIVE.value())
                .build();
    }

}
