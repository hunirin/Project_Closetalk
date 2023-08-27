package team.closetalk.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.closetalk.user.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByLoginId(String LoginId);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByLoginId(String username);

    Optional<UserEntity> findByNickname(String nickname);
}
