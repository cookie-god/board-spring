package kisung.template.board.service.comment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.CommentDto;
import kisung.template.board.entity.Comment;
import kisung.template.board.entity.Feed;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.enums.Role;
import kisung.template.board.repository.comment.CommentRepository;
import kisung.template.board.service.feed.FeedServiceImpl;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostCommentTest {
  @Mock
  private CommentRepository commentRepository;
  @Mock
  private FeedServiceImpl feedService;
  @InjectMocks
  private CommentServiceImpl commentService;
  private JsonNode testData;

  @BeforeEach
  void setUp() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    testData = objectMapper.readTree(new File("src/test/resources/CommentServiceTestData.json"));
  }

  @Test
  @DisplayName("댓글 작성 성공")
  void createComment_success() {
    // given
    JsonNode data = testData.get("postCommentReq").get("validComment");
    CommentDto.PostCommentsReq postCommentsReq = makePostCommentReq(data.get("feedId").asLong(), data.get("parentCommentId").asLong(), data.get("content").asText());
    Feed feed = makeFeedEntity();
    UserInfo userInfo = makeUserInfoEntity();
    Comment comment = makeCommentEntity(feed, null, userInfo, 1L, postCommentsReq.getContent());

    when(feedService.retrieveFeedEntity(any(Long.class))).thenReturn(feed);
    when(commentRepository.save(any(Comment.class))).thenReturn(comment);

    // when
    CommentDto.PostCommetsRes postCommetsRes = commentService.createComments(postCommentsReq, userInfo);

    // then
    assertEquals(1L, postCommetsRes.getCommentId()); // 댓글 아이디 체크
    assertEquals(1L, feed.getCommentCnt()); // 조회수 증가
  }

  @Test
  @DisplayName("답글 작성 성공")
  void createCommentWithParentComment_success() {
    // given
    JsonNode data = testData.get("postCommentReq").get("validCommentWithParentComment");
    CommentDto.PostCommentsReq postCommentsReq = makePostCommentReq(data.get("feedId").asLong(), data.get("parentCommentId").asLong(), data.get("content").asText());
    Feed feed = makeFeedEntity();
    UserInfo userInfo = makeUserInfoEntity();
    Comment parentComment = makeCommentEntity(feed, null, userInfo, postCommentsReq.getParentCommentId(), "부모 댓글 내용");
    Comment comment = makeCommentEntity(feed, parentComment, userInfo, 2L, postCommentsReq.getContent());

    when(feedService.retrieveFeedEntity(any(Long.class))).thenReturn(feed);
    when(commentRepository.findCommentById(any(Long.class))).thenReturn(Optional.of(parentComment));
    when(commentRepository.save(any(Comment.class))).thenReturn(comment);

    // when
    CommentDto.PostCommetsRes postCommetsRes = commentService.createComments(postCommentsReq, userInfo);

    // then
    assertEquals(2L, postCommetsRes.getCommentId()); // 댓글 아이디 체크
    assertNotNull(comment.getParent()); // 부모 댓글 존재
    assertEquals(1L, feed.getCommentCnt()); // 조회수 증가
  }

  @Test
  @DisplayName("답글 작성 실패 - 부모 댓글이 존재 하지 않는 경우")
  void createComment_fail_not_exist_parent_comment_1() {
    // given
    JsonNode data = testData.get("postCommentReq").get("notExistParentComment");
    CommentDto.PostCommentsReq postCommentsReq = makePostCommentReq(data.get("feedId").asLong(), data.get("parentCommentId").asLong(), data.get("content").asText());
    Feed feed = makeFeedEntity();
    UserInfo userInfo = makeUserInfoEntity();
    Comment parentComment = makeCommentEntity(feed, null, userInfo, postCommentsReq.getParentCommentId(), "부모 댓글 내용");
    Comment comment = makeCommentEntity(feed, parentComment, userInfo, 2L, postCommentsReq.getContent());

    when(feedService.retrieveFeedEntity(any(Long.class))).thenReturn(feed);
    when(commentRepository.findCommentById(any(Long.class))).thenReturn(Optional.empty());

    // when + then
    assertThrows(BoardException.class, () -> commentService.createComments(postCommentsReq, userInfo));
  }

  @Test
  @DisplayName("피드 수정 실패 - 피드 아이디가 존재 하지 않는 경우")
  void createComment_fail_empty_feed_id_2() {
    // given
    JsonNode data = testData.get("postCommentReq").get("emptyFeedId");
    CommentDto.PostCommentsReq postCommentsReq = makePostCommentReq(null, data.get("parentCommentId").asLong(), data.get("content").asText());
    UserInfo userInfo = makeUserInfoEntity();

    // when + then
    assertThrows(BoardException.class, () -> commentService.createComments(postCommentsReq, userInfo));
  }

  @Test
  @DisplayName("피드 수정 실패 - 댓글 내용이 존재 하지 않는 경우")
  void createComment_fail_empty_content_id_3() {
    // given
    JsonNode data = testData.get("postCommentReq").get("emptyContent");
    CommentDto.PostCommentsReq postCommentsReq = makePostCommentReq(data.get("feedId").asLong(), data.get("parentCommentId").asLong(), null);
    UserInfo userInfo = makeUserInfoEntity();

    // when + then
    assertThrows(BoardException.class, () -> commentService.createComments(postCommentsReq, userInfo));
  }

  @Test
  @DisplayName("피드 수정 실패 - 부모 댓글 아이디가 존재 하지 않는 경우")
  void createComment_fail_empty_parent_comment_id_4() {
    // given
    JsonNode data = testData.get("postCommentReq").get("emptyParentCommentId");
    CommentDto.PostCommentsReq postCommentsReq = makePostCommentReq(data.get("feedId").asLong(), null, data.get("content").asText());
    UserInfo userInfo = makeUserInfoEntity();

    // when + then
    assertThrows(BoardException.class, () -> commentService.createComments(postCommentsReq, userInfo));
  }

  @Test
  @DisplayName("피드 수정 실패 - 댓글 길이가 300자를 넘는 경우")
  void createComment_fail_invalid_comment_length_5() {
    // given
    JsonNode data = testData.get("postCommentReq").get("inValidContentLength");
    CommentDto.PostCommentsReq postCommentsReq = makePostCommentReq(data.get("feedId").asLong(), data.get("parentCommentId").asLong(), data.get("content").asText());
    UserInfo userInfo = makeUserInfoEntity();

    // when + then
    assertThrows(BoardException.class, () -> commentService.createComments(postCommentsReq, userInfo));
  }

  private CommentDto.PostCommentsReq makePostCommentReq(Long feedId, Long parentCommentId, String content) {
   return CommentDto.PostCommentsReq.builder()
       .feedId(feedId)
       .parentCommentId(parentCommentId)
       .content(content)
       .build();
  }

  private Comment makeCommentEntity(Feed feed, Comment parentComment, UserInfo userInfo, Long commentId, String content) {
    LocalDateTime now = LocalDateTime.now();
    return Comment.builder()
        .id(commentId)
        .feed(feed)
        .parent(parentComment)
        .userInfo(userInfo)
        .content(content)
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

  private Feed makeFeedEntity() {
    LocalDateTime now = LocalDateTime.now();
    return Feed.builder()
        .id(1L)
        .content("내용")
        .commentCnt(0L)
        .bookmarkCnt(0L)
        .createdAt(now)
        .updatedAt(now)
        .status(ACTIVE.value())
        .build();
  }
}
