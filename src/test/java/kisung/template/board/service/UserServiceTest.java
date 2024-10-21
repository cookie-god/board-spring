package kisung.template.board.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kisung.template.board.dto.UserDto;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.time.LocalDateTime;

import static kisung.template.board.enums.Status.ACTIVE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {
  @Mock
  private UserRepository userRepository; // UserRepository Mock
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
  void createUser() {
    // given
    JsonNode validUser = testData.get("postUserReq").get("validUser");
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    LocalDateTime now = LocalDateTime.now();

    //when
    UserInfo userInfo = UserInfo.builder()
        .email(validUser.get("email").asText())
        .nickname(validUser.get("nickname").asText())
        .password(validUser.get("password").asText())
        .createdAt(now)
        .updatedAt(now)
        .status(ACTIVE.name())
        .build();

    userInfo = userInfo.hashPassword(bCryptPasswordEncoder); // 해시 패스워드 생성
    when(userRepository.save(any(UserInfo.class))).thenReturn(userInfo);

//    UserDto.PostUsersRes postUsersRes = UserDto.PostUsersRes.builder()
//        .userId(userInfo.getId())
//        .build();
    //then

//    Assertions.assertEquals(postUsersRes.getUserId(), 1L);
  }

}