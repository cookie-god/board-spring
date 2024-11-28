package kisung.template.board.service.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.config.jwt.JwtTokenProvider;
import kisung.template.board.dto.UserDto;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.enums.Role;
import kisung.template.board.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

import static kisung.template.board.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostLoginTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private JwtTokenProvider jwtTokenProvider;
  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  @InjectMocks
  private UserServiceImpl userService;
  private JsonNode testData;

  @BeforeEach
  void setUp() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    testData = objectMapper.readTree(new File("src/test/resources/UserServiceTestData.json"));
  }

  @Test
  @DisplayName("로그인 성공")
  void loginUser_success() {
    // given
    JsonNode data = testData.get("postLoginReq").get("validUser");
    UserDto.PostLoginReq postLoginReq = makePostLoginReq(data.get("email").asText(), data.get("password").asText());
    UserInfo userInfo = makeUserInfoEntity(postLoginReq.getEmail(), postLoginReq.getEmail());

    when(userRepository.findUserInfoByEmail(any(String.class))).thenReturn(Optional.of(userInfo));
    when(bCryptPasswordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
    when(jwtTokenProvider.createAccessToken(any(UserDto.UserBasicInfo.class))).thenReturn("token");

    //when
    UserDto.PostLoginRes result = userService.login(postLoginReq);

    // then
    assertEquals("token", result.getToken());  // userId 검증
  }

  @Test
  @DisplayName("로그인 실패 - 유효하지 않은 이메일")
  void loginUser_fail_invalid_email() {
    // given
    JsonNode data = testData.get("postLoginReq").get("invalidUserByEmail");
    UserDto.PostLoginReq postLoginReq = makePostLoginReq(data.get("email").asText(), data.get("password").asText());

    //when, then
    assertThrows(BoardException.class, () -> userService.login(postLoginReq));  // userId 검증
  }

  @Test
  @DisplayName("로그인 실패 - 유효하지 않은 비밀번호")
  void loginUser_fail_invalid_password() {
    // given
    JsonNode data = testData.get("postLoginReq").get("invalidUserByPassword");
    UserDto.PostLoginReq postLoginReq = makePostLoginReq(data.get("email").asText(), data.get("password").asText());

    //when, then
    assertThrows(BoardException.class, () -> userService.login(postLoginReq));  // userId 검증
  }

  @Test
  @DisplayName("로그인 실패 - 존재하지 않는 이메일")
  void loginUser_fail_non_exist_email() {
    // given
    JsonNode data = testData.get("postLoginReq").get("validUser");
    UserDto.PostLoginReq postLoginReq = makePostLoginReq(data.get("email").asText(), data.get("password").asText());

    when(userRepository.findUserInfoByEmail(any(String.class))).thenReturn(Optional.empty());

    //when, then
    assertThrows(BoardException.class, () -> userService.login(postLoginReq));  // userId 검증
  }

  @Test
  @DisplayName("로그인 실패 - 일치하지 않는 비밀번호")
  void loginUser_fail_not_match_password() {
    // given
    JsonNode data = testData.get("postLoginReq").get("validUser");
    UserDto.PostLoginReq postLoginReq = makePostLoginReq(data.get("email").asText(), data.get("password").asText());
    UserInfo userInfo = makeUserInfoEntity(postLoginReq.getEmail(), postLoginReq.getEmail());

    when(userRepository.findUserInfoByEmail(any(String.class))).thenReturn(Optional.of(userInfo));
    when(bCryptPasswordEncoder.matches(any(String.class), any(String.class))).thenReturn(false);

    //when, then
    assertThrows(BoardException.class, () -> userService.login(postLoginReq));  // userId 검증
  }

  @Test
  @DisplayName("로그인 실패 - 이메일 존재 하지 않음")
  void loginUser_fail_empty_email() {
    //given
    JsonNode data = testData.get("postLoginReq").get("emptyEmail");
    UserDto.PostLoginReq postLoginReq = makePostLoginReq(null, data.get("password").asText());

    //when, then
    assertThrows(BoardException.class, () -> userService.login(postLoginReq));
  }

  @Test
  @DisplayName("로그인 실패 - 비밀번호 존재 하지 않음")
  void loginUser_fail_empty_password() {
    //given
    JsonNode data = testData.get("postLoginReq").get("emptyPassword");
    UserDto.PostLoginReq postLoginReq = makePostLoginReq(data.get("email").asText(), null);

    //when, then
    assertThrows(BoardException.class, () -> userService.login(postLoginReq));
  }

  private UserDto.PostLoginReq makePostLoginReq(String email, String password) {
    return UserDto.PostLoginReq.builder()
        .email(email)
        .password(password)
        .build();
  }

  private UserInfo makeUserInfoEntity(String email, String password) {
    LocalDateTime now = LocalDateTime.now();
    return UserInfo.builder()
        .id(1L)
        .email(email)
        .nickname("쿠키")
        .password(password)
        .role(Role.USER.value())
        .createdAt(now)
        .updatedAt(now)
        .status(ACTIVE.value())
        .build();
  }
}
