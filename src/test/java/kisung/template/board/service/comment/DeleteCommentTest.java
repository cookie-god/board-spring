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
import static kisung.template.board.enums.Status.INACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteCommentTest {
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
    @DisplayName("댓글 삭제 성공")
    void deleteComment_success() {
        // given
        JsonNode data = testData.get("deleteCommentReq").get("validData");
        CommentDto.DeleteCommentsReq deleteCommentsReq = makeDeleteCommentReq(data.get("commentId").asLong());
        Feed feed = makeFeedEntity();
        UserInfo userInfo = makeUserInfoEntity();
        Comment comment = makeCommentEntity(feed, null, userInfo, deleteCommentsReq.getCommentId(), "댓글");
        when(commentRepository.findCommentById(any(Long.class))).thenReturn(Optional.of(comment));

        // when
        CommentDto.DeleteCommentsRes deleteCommentsRes = commentService.deleteComments(deleteCommentsReq, userInfo);

        // then
        assertEquals(comment.getId(), deleteCommentsRes.getCommentId());
        assertEquals(comment.getStatus(), INACTIVE.value());
    }

    @Test
    @DisplayName("답글 삭제 실패 - 댓글이 존재하지 않는 경우")
    void deleteComment_fail_not_exist_comment() {
        // given
        JsonNode data = testData.get("deleteCommentReq").get("validData");
        CommentDto.DeleteCommentsReq deleteCommentsReq = makeDeleteCommentReq(data.get("commentId").asLong());
        UserInfo userInfo = makeUserInfoEntity();
        when(commentRepository.findCommentById(any(Long.class))).thenReturn(Optional.empty());

        //when, then
        assertThrows(BoardException.class, () -> commentService.deleteComments(deleteCommentsReq, userInfo));
    }

    @Test
    @DisplayName("답글 수정 실패 - 댓글 아이디가 존재하지 않는 경우")
    void editComment_fail_empty_comment_id() {
        // given
        JsonNode data = testData.get("deleteCommentReq").get("validData");
        CommentDto.DeleteCommentsReq deleteCommentsReq = makeDeleteCommentReq(data.get("commentId").asLong());
        UserInfo userInfo = makeUserInfoEntity();

        //when, then
        assertThrows(BoardException.class, () -> commentService.deleteComments(deleteCommentsReq, userInfo));
    }

    private CommentDto.DeleteCommentsReq makeDeleteCommentReq(Long commentId) {
        return CommentDto.DeleteCommentsReq.builder()
                .commentId(commentId)
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
