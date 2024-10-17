package kisung.template.board.repository.user;

import kisung.template.board.entity.UserInfo;
import kisung.template.board.repository.user.custom.CustomUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <UserInfo, Long>, CustomUserRepository {
}
