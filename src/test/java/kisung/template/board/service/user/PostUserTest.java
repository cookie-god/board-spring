package kisung.template.board.service.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.UserDto;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.repository.user.UserRepository;
import kisung.template.board.service.UserService;
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

import static kisung.template.board.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostUserTest {
  @Mock
  private UserRepository userRepository; // UserRepository Mock
  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  @InjectMocks
  private UserService userService; // Mock 객체들을 주입받는 실제 테스트 대상

  private ObjectMapper objectMapper;
  private JsonNode testData;

  @BeforeEach
  void setUp() throws Exception {
    objectMapper = new ObjectMapper();
    testData = objectMapper.readTree(new File("src/test/resources/UserServiceTestData.json"));
  }

  @Test
  @DisplayName("회원가입 성공")
  void createUser_success() {
    // given
    JsonNode validUser = testData.get("postUserReq").get("validUser");
    UserDto.PostUserReq postUserReq = makePostUserReq(validUser.get("email").asText(), validUser.get("nickname").asText(), validUser.get("password").asText());

    // 유저 엔티티 생성
    UserInfo userInfo = makeUserInfoEntity(postUserReq.getEmail(), postUserReq.getNickname(), postUserReq.getPassword());

    // when
    when(userRepository.save(any(UserInfo.class))).thenReturn(userInfo);
    UserDto.PostUsersRes result = userService.createUser(postUserReq);

    // then
    assertNotNull(result);
    assertEquals(userInfo.getId(), result.getUserId());  // userId 검증
  }

  @Test
  @DisplayName("회원가입 실패 - 유효하지 않은 이메일")
  void createUser_fail_invalid_email() {
    //given
    JsonNode inValidUserByEmail = testData.get("postUserReq").get("invalidUserByEmail");
    UserDto.PostUserReq postUserReq = makePostUserReq(inValidUserByEmail.get("email").asText(), inValidUserByEmail.get("nickname").asText(), inValidUserByEmail.get("password").asText());

    //then
    assertThrows(BoardException.class, () -> userService.createUser(postUserReq));
  }

  @Test
  @DisplayName("회원가입 실패 - 유효하지 않은 비밀번호")
  void createUser_fail_invalid_password() {
    //given
    JsonNode inValidUserByEmail = testData.get("postUserReq").get("invalidUserByPassword");
    UserDto.PostUserReq postUserReq = makePostUserReq(inValidUserByEmail.get("email").asText(), inValidUserByEmail.get("nickname").asText(), inValidUserByEmail.get("password").asText());

    //then
    assertThrows(BoardException.class, () -> userService.createUser(postUserReq));
  }

  @Test
  @DisplayName("회원가입 실패 - 유효하지 않은 닉네임")
  void createUser_fail_invalid_nickname() {
    //given
    JsonNode inValidUserByEmail = testData.get("postUserReq").get("invalidUserByNickname");
    UserDto.PostUserReq postUserReq = makePostUserReq(inValidUserByEmail.get("email").asText(), inValidUserByEmail.get("nickname").asText(), inValidUserByEmail.get("password").asText());

    //then
    assertThrows(BoardException.class, () -> userService.createUser(postUserReq));
  }

  @Test
  @DisplayName("회원가입 실패 - 중복된 이메일")
  void createUser_fail_duplicate_email() {
    //given
    JsonNode validUser = testData.get("postUserReq").get("validUser");
    UserDto.PostUserReq postUserReq = makePostUserReq(validUser.get("email").asText(), validUser.get("nickname").asText(), validUser.get("password").asText());

    //when
    when(userRepository.existsByEmail(anyString())).thenReturn(true);

    //then
    assertThrows(BoardException.class, () -> userService.createUser(postUserReq));
  }

  @Test
  @DisplayName("회원가입 실패 - 중복된 닉네임")
  void createUser_fail_duplicate_nickname() {
    //given
    JsonNode validUser = testData.get("postUserReq").get("validUser");
    UserDto.PostUserReq postUserReq = makePostUserReq(validUser.get("email").asText(), validUser.get("nickname").asText(), validUser.get("password").asText());

    //when
    when(userRepository.existsByNickname(anyString())).thenReturn(true);

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
      .createdAt(now)
      .updatedAt(now)
      .status(ACTIVE.value())
      .build();
  }

}