package dnd.donworry.repository.impl;

import static dnd.donworry.domain.entity.QUser.*;

import java.util.Optional;

import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.entity.User;
import dnd.donworry.exception.CustomException;
import dnd.donworry.repository.Support.Querydsl4RepositorySupport;
import dnd.donworry.repository.custom.UserRepositoryCustom;

public class UserRepositoryImpl extends Querydsl4RepositorySupport implements UserRepositoryCustom {
	public UserRepositoryImpl() {
		super(User.class);
	}

	@Override
	public User findByEmailCustom(String email) {
		return Optional.ofNullable(
			selectFrom(user)
				.where(user.email.eq(email))
				.fetchFirst()
		).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
	}
}
