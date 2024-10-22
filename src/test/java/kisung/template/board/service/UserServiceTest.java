package kisung.template.board.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kisung.template.board.dto.UserDto;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.time.LocalDateTime;

import static kisung.template.board.enums.Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
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
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createUser_Success() {
    // given
    JsonNode validUser = testData.get("postUserReq").get("validUser");
    UserDto.PostUserReq postUserReq = UserDto.PostUserReq.builder()
        .email(validUser.get("email").asText())
        .nickname(validUser.get("nickname").asText())
        .password(validUser.get("password").asText())
        .build();
    LocalDateTime now = LocalDateTime.now();


    // 유저 엔티티 생성
    UserInfo userInfo = UserInfo.builder()
        .id(1L)
        .email(postUserReq.getEmail())
        .nickname(postUserReq.getNickname())
        .password(postUserReq.getPassword())
        .createdAt(now)
        .updatedAt(now)
        .status(ACTIVE.name())
        .build();

    // 패스워드 해시값 모킹
    when(bCryptPasswordEncoder.encode(postUserReq.getPassword())).thenReturn("hashedPassword");

    when(userService.CreateUserEntity(postUserReq)).thenReturn(userInfo);

    // 저장된 유저 정보 모킹
    when(userRepository.save(any(UserInfo.class))).thenReturn(userInfo);

    UserDto.PostUsersRes postUsersRes = userService.createUser(postUserReq);

//    // then
//    assertNotNull(postUsersRes);
//    assertEquals(postUsersRes.getUserId(), 1L);

    // 저장된 객체 검증
    ArgumentCaptor<UserInfo> captor = ArgumentCaptor.forClass(UserInfo.class);
    verify(userRepository).save(captor.capture());
    UserInfo savedUserInfo = captor.getValue();

    assertEquals(savedUserInfo.getEmail(), postUserReq.getEmail());
    assertEquals(savedUserInfo.getNickname(), postUserReq.getNickname());
    assertEquals(savedUserInfo.getPassword(), "hashedPassword");
  }

}