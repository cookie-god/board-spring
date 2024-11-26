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
public class PatchUserPasswordTest {
  @Mock
  private UserRepository userRepository;
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
  @DisplayName("유저 비밀 번호 변경 성공")
  void editUserPassword_success() {
    // given
    JsonNode data = testData.get("patchUserPasswordReq").get("validData");
    UserDto.PatchUserPasswordReq patchUserPasswordReq = makePatchUserPasswordReq(data.get("email").asText(), data.get("password").asText(), data.get("newPassword").asText());
    UserInfo userInfo = makeUserInfoEntity(patchUserPasswordReq.getEmail(), patchUserPasswordReq.getPassword());

    when(userRepository.findUserInfoByEmail(any(String.class))).thenReturn(Optional.of(userInfo));
    when(bCryptPasswordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
    when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn(patchUserPasswordReq.getNewPassword());

    //when
    userService.editUserPassword(patchUserPasswordReq);

    // then
    assertEquals(userInfo.getPassword(), patchUserPasswordReq.getNewPassword());  // userId 검증
  }

  @Test
  @DisplayName("유저 비밀 번호 변경 실패 - 유효 하지 않은 이메일")
  void editUserPassword_fail_invalid_email() {
    // given
    JsonNode data = testData.get("patchUserPasswordReq").get("invalidDataByEmail");
    UserDto.PatchUserPasswordReq patchUserPasswordReq = makePatchUserPasswordReq(data.get("email").asText(), data.get("password").asText(), data.get("newPassword").asText());

    //when, then
    assertThrows(BoardException.class, () -> userService.editUserPassword(patchUserPasswordReq));  // userId 검증
  }

  @Test
  @DisplayName("유저 기존 비밀 번호 변경 실패 - 유효 하지 않은 기존 비밀 번호")
  void editUserPassword_fail_invalid_password() {
    // given
    JsonNode data = testData.get("patchUserPasswordReq").get("invalidDataByPassword");
    UserDto.PatchUserPasswordReq patchUserPasswordReq = makePatchUserPasswordReq(data.get("email").asText(), data.get("password").asText(), data.get("newPassword").asText());

    //when, then
    assertThrows(BoardException.class, () -> userService.editUserPassword(patchUserPasswordReq));  // userId 검증
  }

  @Test
  @DisplayName("유저 새로운 비밀 번호 변경 실패 - 유효 하지 않은 새로운 비밀 번호")
  void editUserPassword_fail_invalid_new_password() {
    // given
    JsonNode data = testData.get("patchUserPasswordReq").get("invalidDataByNewPassword");
    UserDto.PatchUserPasswordReq patchUserPasswordReq = makePatchUserPasswordReq(data.get("email").asText(), data.get("password").asText(), data.get("newPassword").asText());

    //when, then
    assertThrows(BoardException.class, () -> userService.editUserPassword(patchUserPasswordReq));  // userId 검증
  }

  @Test
  @DisplayName("유저 새로운 비밀 번호 변경 실패 - 존재 하지 않는 이메일")
  void editUserPassword_fail_non_exist_email() {
    // given
    JsonNode data = testData.get("patchUserPasswordReq").get("validData");
    UserDto.PatchUserPasswordReq patchUserPasswordReq = makePatchUserPasswordReq(data.get("email").asText(), data.get("password").asText(), data.get("newPassword").asText());

    when(userRepository.findUserInfoByEmail(any(String.class))).thenReturn(Optional.empty());

    //when, then
    assertThrows(BoardException.class, () -> userService.editUserPassword(patchUserPasswordReq));  // userId 검증
  }

  @Test
  @DisplayName("유저 새로운 비밀 번호 변경 실패 - 일치 하지 않는 기존 비밀 번호")
  void editUserPassword_fail_not_match_password() {
    // given
    JsonNode data = testData.get("patchUserPasswordReq").get("validData");
    UserDto.PatchUserPasswordReq patchUserPasswordReq = makePatchUserPasswordReq(data.get("email").asText(), data.get("password").asText(), data.get("newPassword").asText());
    UserInfo userInfo = makeUserInfoEntity(patchUserPasswordReq.getEmail(), patchUserPasswordReq.getPassword());

    when(userRepository.findUserInfoByEmail(any(String.class))).thenReturn(Optional.of(userInfo));
    when(bCryptPasswordEncoder.matches(any(String.class), any(String.class))).thenReturn(false);

    //when, then
    assertThrows(BoardException.class, () -> userService.editUserPassword(patchUserPasswordReq));  // userId 검증
  }

  @Test
  @DisplayName("유저 새로운 비밀 번호 변경 실패 - 이메일 존재 하지 않음")
  void editUserPassword_fail_empty_email() {
    //given
    JsonNode data = testData.get("patchUserPasswordReq").get("emptyEmail");
    UserDto.PatchUserPasswordReq patchUserPasswordReq = makePatchUserPasswordReq(null, data.get("password").asText(), data.get("newPassword").asText());

    //when, then
    assertThrows(BoardException.class, () -> userService.editUserPassword(patchUserPasswordReq));
  }

  @Test
  @DisplayName("유저 새로운 비밀 번호 변경 실패 - 기존 비밀 번호 존재 하지 않음")
  void createUser_fail_empty_password() {
    //given
    JsonNode data = testData.get("patchUserPasswordReq").get("emptyPassword");
    UserDto.PatchUserPasswordReq patchUserPasswordReq = makePatchUserPasswordReq(data.get("email").asText(), null, data.get("newPassword").asText());

    //when, then
    assertThrows(BoardException.class, () -> userService.editUserPassword(patchUserPasswordReq));
  }

  @Test
  @DisplayName("유저 새로운 비밀 번호 변경 실패 - 새로운 비밀 번호 존재 하지 않음")
  void createUser_fail_empty_new_password() {
    //given
    JsonNode data = testData.get("patchUserPasswordReq").get("emptyNewPassword");
    UserDto.PatchUserPasswordReq patchUserPasswordReq = makePatchUserPasswordReq(data.get("email").asText(), data.get("password").asText(), null);

    //when, then
    assertThrows(BoardException.class, () -> userService.editUserPassword(patchUserPasswordReq));
  }

  private UserDto.PatchUserPasswordReq makePatchUserPasswordReq(String email, String password, String newPassword) {
    return UserDto.PatchUserPasswordReq.builder()
        .email(email)
        .password(password)
        .newPassword(newPassword)
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
