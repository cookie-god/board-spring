package kisung.template.board.entity;

import jakarta.persistence.*;
import kisung.template.board.dto.UserDto;
import kisung.template.board.entity.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static kisung.template.board.enums.Role.USER;
import static kisung.template.board.enums.Status.ACTIVE;

@Entity
@Table(name = "USER_INFO")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString
public class UserInfo extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "nickname")
  private String nickname;

  @Column(name = "role")
  private String role;

  /**
   * 유저 엔티티 인스턴스 생성하는 정적 팩토리 메서드
   */
  public static UserInfo of(String email, String nickname, String password) {
    LocalDateTime now = LocalDateTime.now();
    return UserInfo.builder()
            .email(email)
            .nickname(nickname)
            .password(password)
            .role(USER.value())
            .createdAt(now)
            .updatedAt(now)
            .status(ACTIVE.value())
            .build();
  }

  public void setInitPassword() {
    this.password = null;
  }

  // 비밀번호 암호화
  public UserInfo hashPassword(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(this.password);
    return this;
  }

  // 비밀번호 암호화
  public void changePassword(PasswordEncoder passwordEncoder, String newPassword) {
    this.password = passwordEncoder.encode(newPassword);
  }

  // 비밀번호 확인
  public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
    return passwordEncoder.matches(plainPassword, this.password);
  }
}
