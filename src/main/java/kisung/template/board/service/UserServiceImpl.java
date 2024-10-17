package kisung.template.board.service;

import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.UserDto;
import kisung.template.board.entity.User;
import kisung.template.board.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static kisung.template.board.common.code.ErrorCode.*;
import static kisung.template.board.enums.Status.ACTIVE;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  /**
   * 유저 회원 가입 서비스 메서드
   */
  @Override
  @Transactional
  public UserDto.PostUsersRes createUser(UserDto.PostUserReq postUserReq) {
    validate(postUserReq);
    User user = CreateUserEntity(postUserReq); // 유저 생성
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    user = user.hashPassword(bCryptPasswordEncoder); // 해시 패스워드 생성
    user = userRepository.save(user); // 유저 저장
    return UserDto.PostUsersRes.builder()
      .userId(user.getId())
      .build();
  }

  @Override
  public UserDto.PostUserLoginRes login(UserDto.PostUserLoginReq postUserLoginReq) {
    validate(postUserLoginReq);
    return UserDto.PostUserLoginRes.builder()
        .userId(1L)
        .email(postUserLoginReq.getEmail())
        .nickname("ccec")
        .build();
  }

  /**
   * 유저 회원 가입 validate
   * request에 대한 값 체크하는 메서드
   * 1. 이메일 값 존재 여부 체크
   * 2. 이메일 정규식 체크
   * 3. 비밀번호 값 존재 여부 체크
   * 4. 비밀번호 정규식 체크
   * 5. 닉네임 존재 여부 체크
   * 6. 닉네임 10자 이내 체크
   */
  public void validate(UserDto.PostUserReq postUserReq) {
    if (postUserReq.getEmail() == null || postUserReq.getEmail().isEmpty()) {
      throw new BoardException(NON_EXIST_EMAIL);
    }
    if (!postUserReq.isEmail()) {
      throw new BoardException(INVALID_EMAIL);
    }
    if (postUserReq.getPassword() == null || postUserReq.getPassword().isEmpty()) {
      throw new BoardException(NON_EXIST_PASSWORD);
    }
    if (!postUserReq.isPassword()) {
      throw new BoardException(INVALID_PASSWORD);
    }
    if (postUserReq.getNickname() == null || postUserReq.getNickname().isEmpty()) {
      throw new BoardException(NON_EXIST_NICKNAME);
    }
    if (!postUserReq.isNickname()) {
      throw new BoardException(INVALID_NICKNAME);
    }
  }

  /**
   * 유저 로그인 validate
   * request에 대한 값 체크하는 메서드
   * 1. 이메일 값 존재 여부 체크
   * 2. 이메일 정규식 체크
   * 3. 비밀번호 값 존재 여부 체크
   * 4. 비밀번호 정규식 체크
   * */
  public void validate(UserDto.PostUserLoginReq postUserLoginReq) {
    if (postUserLoginReq.getEmail() == null || postUserLoginReq.getEmail().isEmpty()) {
      throw new BoardException(NON_EXIST_EMAIL);
    }
    if (!postUserLoginReq.isEmail()) {
      throw new BoardException(INVALID_EMAIL);
    }
    if (postUserLoginReq.getPassword() == null || postUserLoginReq.getPassword().isEmpty()) {
      throw new BoardException(NON_EXIST_PASSWORD);
    }
    if (!postUserLoginReq.isPassword()) {
      throw new BoardException(INVALID_PASSWORD);
    }
  }

  /**
   * 유저 엔티티 인스턴스 생성하는 메서드
   */
  private User CreateUserEntity(UserDto.PostUserReq postUserReq) {
    LocalDateTime now = LocalDateTime.now();
    return User.builder()
      .email(postUserReq.getEmail())
      .nickname(postUserReq.getNickname())
      .password(postUserReq.getPassword())
      .createdAt(now)
      .updatedAt(now)
      .status(ACTIVE.name())
      .build();
  }
}
