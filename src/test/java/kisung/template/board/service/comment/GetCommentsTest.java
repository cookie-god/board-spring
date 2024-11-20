package kisung.template.board.service.comment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.CommentDto;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.enums.Role;
import kisung.template.board.repository.comment.CommentRepository;
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

import static kisung.template.board.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCommentsTest {
  @Mock
  private CommentRepository commentRepository;
  @InjectMocks
  private CommentServiceImpl commentService;
  private JsonNode testData;

  @BeforeEach
  void setUp() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    testData = objectMapper.readTree(new File("src/test/resources/CommentServiceTestData.json"));
  }

  @Test
  @DisplayName("댓글 조회 성공")
  void retrieveComments_success() {
    // given
    JsonNode data = testData.get("getCommentsReq").get("validData");
    CommentDto.GetCommentsReq getCommentsReq = makeGetCommentsReq(data.get("feedId").asLong(), data.get("commentId").asLong(), data.get("size").asInt());
    UserInfo userInfo = makeUserInfoEntity();
    List<CommentDto.CommentRawInfo> commentRawInfos = makeCommentRawInfoList(getCommentsReq.getSize());

    when(commentRepository.countCommentInfos(any(CommentDto.GetCommentsReq.class))).thenReturn(10L);
    when(commentRepository.findCommentInfos(any(CommentDto.GetCommentsReq.class))).thenReturn(commentRawInfos);

    // when
    CommentDto.GetCommentsRes getCommentsRes = commentService.retrieveComments(getCommentsReq, userInfo);

    // then
    assertEquals(10L, getCommentsRes.getCount());
    assertEquals(getCommentsReq.getSize(), getCommentsRes.getCommentInfos().size());
  }

  @Test
  @DisplayName("댓글 조회 실패 - 피드 아이디가 없는 경우 실패")
  void retrieveComments_fail_empty_feed_id() {
    //given
    JsonNode data = testData.get("getCommentsReq").get("emptyFeedId");
    CommentDto.GetCommentsReq getCommentsReq = makeGetCommentsReq(null, data.get("commentId").asLong(), data.get("size").asInt());
    UserInfo userInfo = makeUserInfoEntity();

    //when, then
    assertThrows(BoardException.class, () -> commentService.retrieveComments(getCommentsReq, userInfo));
  }

  @Test
  @DisplayName("댓글 조회 실패 - 댓글 아이디가 없는 경우 실패")
  void retrieveComments_fail_empty_comment_id() {
    //given
    JsonNode data = testData.get("getCommentsReq").get("emptyCommentId");
    CommentDto.GetCommentsReq getCommentsReq = makeGetCommentsReq(data.get("feedId").asLong(), null, data.get("size").asInt());
    UserInfo userInfo = makeUserInfoEntity();

    //when, then
    assertThrows(BoardException.class, () -> commentService.retrieveComments(getCommentsReq, userInfo));
  }

  @Test
  @DisplayName("댓글 조회 실패 - 페이지 사이즈가 없는 경우 실패")
  void retrieveComments_fail_empty_size() {
    //given
    JsonNode data = testData.get("getCommentsReq").get("emptyPageSize");
    CommentDto.GetCommentsReq getCommentsReq = makeGetCommentsReq(data.get("feedId").asLong(), data.get("commentId").asLong(), null);
    UserInfo userInfo = makeUserInfoEntity();

    //when, then
    assertThrows(BoardException.class, () -> commentService.retrieveComments(getCommentsReq, userInfo));
  }


  private CommentDto.GetCommentsReq makeGetCommentsReq(Long feedId, Long commentId, Integer size) {
    return CommentDto.GetCommentsReq.builder()
      .feedId(feedId)
      .commentId(commentId)
      .size(size)
      .build();
  }

  List<CommentDto.CommentRawInfo> makeCommentRawInfoList(int size) {
    List<CommentDto.CommentRawInfo> commentRawInfos = new ArrayList<>();
    for (long i = 1; i <= size; i++) {
      commentRawInfos.add(
        CommentDto.CommentRawInfo.builder()
          .commentId(i)
          .childCommentCnt(10L)
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .build()
      );
    }
    return commentRawInfos;
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
