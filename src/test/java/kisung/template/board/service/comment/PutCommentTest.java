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
import org.junit.jupiter.api.Assertions;
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
public class PutCommentTest {
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentServiceImpl commentService;
    private JsonNode testData;

    @BeforeEach
    void setup() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        testData = objectMapper.readTree(new File("src/test/resources/CommentServiceTestData.json"));
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void editComment_success() {
        // given
        JsonNode data = testData.get("putCommentReq").get("validData");
        CommentDto.PutCommentsReq putCommentsReq = makePutCommentReq(data.get("commentId").asLong(), data.get("content").asText());
        Feed feed = makeFeedEntity();
        UserInfo userInfo = makeUserInfoEntity();
        Comment comment = makeCommentEntity(feed, null, userInfo, putCommentsReq.getCommentId(), "이전 댓글");
        when(commentRepository.findCommentById(any(Long.class))).thenReturn(Optional.of(comment));

        // when
        CommentDto.PutCommentsRes putCommentsRes = commentService.editComments(putCommentsReq, userInfo);

        // then
        assertEquals(comment.getId(), putCommentsRes.getCommentId());
        assertNotEquals("이전 댓글", comment.getContent());
    }

    @Test
    @DisplayName("답글 수정 실패 - 댓글이 존재하지 않는 경우")
    void editComment_fail_not_exist_comment() {
        // given
        JsonNode data = testData.get("putCommentReq").get("validData");
        CommentDto.PutCommentsReq putCommentsReq = makePutCommentReq(data.get("commentId").asLong(), data.get("content").asText());
        UserInfo userInfo = makeUserInfoEntity();
        when(commentRepository.findCommentById(any(Long.class))).thenReturn(Optional.empty());

        //when, then
        assertThrows(BoardException.class, () -> commentService.editComments(putCommentsReq, userInfo));
    }

    @Test
    @DisplayName("답글 수정 실패 - 댓글 아이디가 존재하지 않는 경우")
    void editComment_fail_empty_comment_id() {
        // given
        JsonNode data = testData.get("putCommentReq").get("emptyCommentId");
        CommentDto.PutCommentsReq putCommentsReq = makePutCommentReq(null, data.get("content").asText());
        UserInfo userInfo = makeUserInfoEntity();

        //when, then
        assertThrows(BoardException.class, () -> commentService.editComments(putCommentsReq, userInfo));
    }

    @Test
    @DisplayName("답글 수정 실패 - 댓글 내용이 존재하지 않는 경우")
    void editComment_fail_empty_content() {
        // given
        JsonNode data = testData.get("putCommentReq").get("emptyContent");
        CommentDto.PutCommentsReq putCommentsReq = makePutCommentReq(data.get("commentId").asLong(), null);
        UserInfo userInfo = makeUserInfoEntity();

        //when, then
        assertThrows(BoardException.class, () -> commentService.editComments(putCommentsReq, userInfo));
    }

    @Test
    @DisplayName("답글 수정 실패 - 댓글 길이가 300자를 넘는 경우")
    void editComment_fail_invalid_comment_length() {
        // given
        JsonNode data = testData.get("putCommentReq").get("inValidContentLength");
        CommentDto.PutCommentsReq putCommentsReq = makePutCommentReq(data.get("commentId").asLong(), data.get("content").asText());
        UserInfo userInfo = makeUserInfoEntity();

        //when, then
        assertThrows(BoardException.class, () -> commentService.editComments(putCommentsReq, userInfo));
    }

    private CommentDto.PutCommentsReq makePutCommentReq(Long commentId, String content) {
        return CommentDto.PutCommentsReq.builder()
                .commentId(commentId)
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
