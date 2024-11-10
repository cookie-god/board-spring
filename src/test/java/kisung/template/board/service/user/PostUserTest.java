package kisung.template.board.service.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kisung.template.board.config.exception.BoardException;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.time.LocalDateTime;

import static kisung.template.board.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostUserTest {
  @Mock
  private UserRepository userRepository; // UserRepository Mock
  @Spy
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  @InjectMocks
  private UserServiceImpl userService; // Mock 객체들을 주입받는 실제 테스트 대상

  private JsonNode testData;

  @BeforeEach
  void setUp() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    bCryptPasswordEncoder = new BCryptPasswordEncoder();
    testData = objectMapper.readTree(new File("src/test/resources/UserServiceTestData.json"));
  }

  @Test
  @DisplayName("회원가입 성공")
  void createUser_success() {
    // given
    JsonNode data = testData.get("postUserReq").get("validUser");
    UserDto.PostUserReq postUserReq = makePostUserReq(data.get("email").asText(), data.get("nickname").asText(), data.get("password").asText());
    UserInfo userInfo = makeUserInfoEntity(postUserReq.getEmail(), postUserReq.getNickname(), bCryptPasswordEncoder.encode(postUserReq.getPassword()));

    when(userRepository.save(any(UserInfo.class))).thenReturn(userInfo);

    // when
    UserDto.PostUsersRes result = userService.createUser(postUserReq);

    // then
    assertNotNull(result);
    assertEquals(userInfo.getId(), result.getUserId());  // userId 검증
  }

  @Test
  @DisplayName("회원가입 실패 - 유효하지 않은 이메일")
  void createUser_fail_invalid_email() {
    //given
    JsonNode data = testData.get("postUserReq").get("invalidUserByEmail");
    UserDto.PostUserReq postUserReq = makePostUserReq(data.get("email").asText(), data.get("nickname").asText(), data.get("password").asText());

    //then
    assertThrows(BoardException.class, () -> userService.createUser(postUserReq));
  }

  @Test
  @DisplayName("회원가입 실패 - 유효하지 않은 비밀번호")
  void createUser_fail_invalid_password() {
    //given
    JsonNode data = testData.get("postUserReq").get("invalidUserByPassword");
    UserDto.PostUserReq postUserReq = makePostUserReq(data.get("email").asText(), data.get("nickname").asText(), data.get("password").asText());

    //then
    assertThrows(BoardException.class, () -> userService.createUser(postUserReq));
  }

  @Test
  @DisplayName("회원가입 실패 - 유효하지 않은 닉네임")
  void createUser_fail_invalid_nickname() {
    //given
    JsonNode data = testData.get("postUserReq").get("invalidUserByNickname");
    UserDto.PostUserReq postUserReq = makePostUserReq(data.get("email").asText(), data.get("nickname").asText(), data.get("password").asText());

    //then
    assertThrows(BoardException.class, () -> userService.createUser(postUserReq));
  }

  @Test
  @DisplayName("회원가입 실패 - 중복된 이메일")
  void createUser_fail_duplicate_email() {
    //given
    JsonNode data = testData.get("postUserReq").get("validUser");
    UserDto.PostUserReq postUserReq = makePostUserReq(data.get("email").asText(), data.get("nickname").asText(), data.get("password").asText());

    when(userRepository.existsByEmail(anyString())).thenReturn(true);

    //then
    assertThrows(BoardException.class, () -> userService.createUser(postUserReq));
  }

  @Test
  @DisplayName("회원가입 실패 - 중복된 닉네임")
  void createUser_fail_duplicate_nickname() {
    //given
    JsonNode data = testData.get("postUserReq").get("validUser");
    UserDto.PostUserReq postUserReq = makePostUserReq(data.get("email").asText(), data.get("nickname").asText(), data.get("password").asText());

    when(userRepository.existsByNickname(anyString())).thenReturn(true);

    //then
    assertThrows(BoardException.class, () -> userService.createUser(postUserReq));
  }

  @Test
  @DisplayName("회원가입 실패 - 이메일 존재 하지 않음")
  void createUser_fail_empty_email() {
    //given
    JsonNode data = testData.get("postUserReq").get("emptyEmail");
    UserDto.PostUserReq postUserReq = makePostUserReq(null, data.get("nickname").asText(), data.get("password").asText());

    //then
    assertThrows(BoardException.class, () -> userService.createUser(postUserReq));
  }

  @Test
  @DisplayName("회원가입 실패 - 비밀번호 존재 하지 않음")
  void createUser_fail_empty_password() {
    //given
    JsonNode data = testData.get("postUserReq").get("emptyPassword");
    UserDto.PostUserReq postUserReq = makePostUserReq(data.get("email").asText(), data.get("nickname").asText(), null);

    //then
    assertThrows(BoardException.class, () -> userService.createUser(postUserReq));
  }

  @Test
  @DisplayName("회원가입 실패 - 닉네임 존재 하지 않음")
  void createUser_fail_empty_nickname() {
    //given
    JsonNode data = testData.get("postUserReq").get("emptyNickname");
    UserDto.PostUserReq postUserReq = makePostUserReq(data.get("email").asText(), null, data.get("password").asText());

    //then
    assertThrows(BoardException.class, () -> userService.createUser(postUserReq));
  }

  private UserDto.PostUserReq makePostUserReq(String email, String nickname, String password) {
    return UserDto.PostUserReq.builder()
      .email(email)
      .nickname(nickname)
      .password(password)
      .build();
  }

  private UserInfo makeUserInfoEntity(String email, String nickname, String password) {
    LocalDateTime now = LocalDateTime.now();
    return UserInfo.builder()
      .id(1L)
      .email(email)
      .nickname(nickname)
      .password(password)
      .role(Role.USER.value())
      .createdAt(now)
      .updatedAt(now)
      .status(ACTIVE.value())
      .build();
  }

}