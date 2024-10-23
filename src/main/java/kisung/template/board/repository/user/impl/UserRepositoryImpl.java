package kisung.template.board.repository.user.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.repository.user.custom.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static kisung.template.board.entity.QUserInfo.userInfo;
import static kisung.template.board.enums.Status.ACTIVE;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements CustomUserRepository {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public boolean existsByEmail(String email) {
    Integer fetchOne = jpaQueryFactory
        .selectOne()
        .from(userInfo)
        .where(
            userInfo.email.eq(email),
            userInfo.status.eq(ACTIVE.value())
        )
        .fetchFirst();
    return fetchOne != null;
  }

  @Override
  public boolean existsByNickname(String nickname) {
    Integer fetchOne = jpaQueryFactory
        .selectOne()
        .from(userInfo)
        .where(
            userInfo.nickname.eq(nickname),
            userInfo.status.eq(ACTIVE.value())
        )
        .fetchFirst();
    return fetchOne != null;
  }

  @Override
  public Optional<UserInfo> findUserInfoByEmail(String email) {
    return Optional.ofNullable(
        jpaQueryFactory
            .select(userInfo)
            .from(userInfo)
            .where(
                userInfo.email.eq(email),
                userInfo.status.eq(ACTIVE.value())
            )
            .fetchFirst()

    );
  }

  @Override
  public Optional<UserInfo> findUserInfoById(Long userId) {
    return Optional.ofNullable(
      jpaQueryFactory
        .select(userInfo)
        .from(userInfo)
        .where(
          userInfo.id.eq(userId),
          userInfo.status.eq(ACTIVE.value())
        )
        .fetchFirst()

    );
  }
}
