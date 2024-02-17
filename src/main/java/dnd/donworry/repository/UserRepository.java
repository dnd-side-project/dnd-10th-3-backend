package dnd.donworry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.donworry.domain.entity.User;
import dnd.donworry.repository.custom.UserRepositoryCustom;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
	Optional<User> findUserByEmail(String email);

	Boolean existsByNickname(String nickname);

	User findByNickname(String nickname);
}
