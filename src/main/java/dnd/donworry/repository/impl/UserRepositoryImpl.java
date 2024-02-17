package dnd.donworry.repository.impl;

import static com.querydsl.jpa.JPAExpressions.*;
import static dnd.donworry.domain.entity.QUser.*;

import java.util.Optional;

import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.entity.User;
import dnd.donworry.exception.CustomException;
import dnd.donworry.repository.custom.UserRepositoryCustom;

public class UserRepositoryImpl implements UserRepositoryCustom {
	@Override
	public User findByEmail(String email) {
		return Optional.ofNullable(
				selectFrom(user)
					.where(user.email.eq(email))
					.fetchOne())
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
			);
	}
}
