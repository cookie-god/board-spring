package kisung.template.board.service;

import kisung.template.board.common.code.ErrorCode;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.UserDto;
import kisung.template.board.entity.User;
import kisung.template.board.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static kisung.template.board.common.code.ErrorCode.*;
import static kisung.template.board.enums.Status.ACTIVE;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public UserDto.PostUsersRes createUser(UserDto.PostUserReq postUserReq) {
    validate(postUserReq);
    User user = CreateUserEntity(postUserReq); // 유저 생성
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    user = user.hashPassword(bCryptPasswordEncoder); // 해시 패스워드 생성
    user = userRepository.save(user);
    return UserDto.PostUsersRes.builder()
      .userId(user.getId())
      .build();
  }

  public void validate(UserDto.PostUserReq postUserReq) {
    if (postUserReq.getEmail() == null || postUserReq.getEmail().length() == 0) { // 이메일 확인
      throw new BoardException(NON_EXIST_EMAIL);
    }

    if (postUserReq.getPassword() == null || postUserReq.getPassword().length() == 0) { // 비밀번호 확인
      throw new BoardException(NON_EXIST_PASSWORD);
    }

    if (postUserReq.getNickname() == null || postUserReq.getNickname().length() == 0) { // 닉네임 확인
      throw new BoardException(NON_EXIST_NICKNAME);
    }
  }

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
