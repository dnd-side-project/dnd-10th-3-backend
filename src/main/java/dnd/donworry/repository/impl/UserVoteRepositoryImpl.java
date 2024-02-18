package dnd.donworry.repository.impl;

import static dnd.donworry.domain.entity.QUserVote.*;

import java.util.Optional;

import dnd.donworry.domain.entity.UserVote;
import dnd.donworry.repository.Support.Querydsl4RepositorySupport;
import dnd.donworry.repository.custom.UserVoteRepositoryCustom;

public class UserVoteRepositoryImpl extends Querydsl4RepositorySupport implements UserVoteRepositoryCustom {
	public UserVoteRepositoryImpl() {
		super(UserVote.class);
	}

	@Override
	public Optional<UserVote> findUserVoteByEmailAndVoteId(String email, Long voteId) {
		return Optional.ofNullable(
			selectFrom(userVote)
				.where(userVote.selection.vote.id.eq(voteId)
					.and(userVote.user.email.eq(email))
				).fetchOne()
		);
	}
}
