package dnd.donworry.repository.custom;

import dnd.donworry.domain.entity.User;

public interface UserRepositoryCustom {

	User findByEmail(String email);
}
