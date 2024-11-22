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
public class GetRepliesTest {
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
  @DisplayName("답글 조회 성공")
  void retrieveReplies_success() {
    // given
    JsonNode data = testData.get("getRepliesReq").get("validData");
    CommentDto.GetRepliesReq getRepliesReq = makeGetRepliesReq(data.get("replyId").asLong(), data.get("commentId").asLong(), data.get("size").asInt());
    UserInfo userInfo = makeUserInfoEntity();
    List<CommentDto.ReplyRawInfo> replyRawInfos = makeReplyRawInfoList(getRepliesReq.getSize());

    when(commentRepository.countReplyInfos(any(CommentDto.GetRepliesReq.class))).thenReturn(10L);
    when(commentRepository.findReplyInfos(any(CommentDto.GetRepliesReq.class))).thenReturn(replyRawInfos);

    // when
    CommentDto.GetRepliesRes getRepliesRes = commentService.retrieveReplies(getRepliesReq, userInfo);

    // then
    assertEquals(10L, getRepliesRes.getCount());
    assertEquals(getRepliesReq.getSize(), getRepliesRes.getReplyInfos().size());
  }

  @Test
  @DisplayName("답글 조회 실패 - 답글 아이디가 없는 경우 실패")
  void retrieveReplies_fail_empty_feed_id() {
    //given
    JsonNode data = testData.get("getRepliesReq").get("emptyReplyId");
    CommentDto.GetRepliesReq getRepliesReq = makeGetRepliesReq(null, data.get("commentId").asLong(), data.get("size").asInt());
    UserInfo userInfo = makeUserInfoEntity();

    //when, then
    assertThrows(BoardException.class, () -> commentService.retrieveReplies(getRepliesReq, userInfo));
  }

  @Test
  @DisplayName("답글 조회 실패 - 상위 댓글 아이디가 없는 경우 실패")
  void retrieveReplies_fail_empty_comment_id() {
    //given
    JsonNode data = testData.get("getRepliesReq").get("emptyCommentId");
    CommentDto.GetRepliesReq getRepliesReq = makeGetRepliesReq(data.get("replyId").asLong(), null, data.get("size").asInt());
    UserInfo userInfo = makeUserInfoEntity();

    //when, then
    assertThrows(BoardException.class, () -> commentService.retrieveReplies(getRepliesReq, userInfo));
  }

  @Test
  @DisplayName("답글 조회 실패 - 페이지 사이즈가 없는 경우 실패")
  void retrieveReplies_fail_empty_size() {
    //given
    JsonNode data = testData.get("getRepliesReq").get("emptyCommentId");
    CommentDto.GetRepliesReq getRepliesReq = makeGetRepliesReq(data.get("replyId").asLong(), data.get("commentId").asLong(), null);
    UserInfo userInfo = makeUserInfoEntity();

    //when, then
    assertThrows(BoardException.class, () -> commentService.retrieveReplies(getRepliesReq, userInfo));
  }


  private CommentDto.GetRepliesReq makeGetRepliesReq(Long replyId, Long commentId, Integer size) {
    return CommentDto.GetRepliesReq.builder()
      .replyId(replyId)
      .commentId(commentId)
      .size(size)
      .build();
  }

  List<CommentDto.ReplyRawInfo> makeReplyRawInfoList(int size) {
    List<CommentDto.ReplyRawInfo> replyRawInfos = new ArrayList<>();
    for (long i = 1; i <= size; i++) {
      replyRawInfos.add(
        CommentDto.ReplyRawInfo.builder()
          .commentId(i)
          .parentCommentId(1L)
          .content("내용")
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .build()
      );
    }
    return replyRawInfos;
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
