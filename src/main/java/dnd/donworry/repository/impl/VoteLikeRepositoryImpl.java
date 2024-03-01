package dnd.donworry.repository.impl;

import static dnd.donworry.domain.entity.QUser.*;
import static dnd.donworry.domain.entity.QVote.*;
import static dnd.donworry.domain.entity.QVoteLike.*;

import java.util.Optional;

import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.domain.entity.VoteLike;
import dnd.donworry.repository.Support.Querydsl4RepositorySupport;
import dnd.donworry.repository.custom.VoteLikeRepositoryCustom;

public class VoteLikeRepositoryImpl extends Querydsl4RepositorySupport implements VoteLikeRepositoryCustom {
	public VoteLikeRepositoryImpl() {
		super(VoteLike.class);
	}

	@Override
	public Optional<VoteLike> findByVoteAndUserCustom(Vote inputVote, User inputUser) {
		return Optional.ofNullable(
			selectFrom(voteLike)
				.leftJoin(voteLike.user, user).fetchJoin()
				.leftJoin(voteLike.vote, vote).fetchJoin()
				.where(voteLike.vote.eq(inputVote)
					.and(voteLike.user.eq(inputUser))
				).fetchOne()
		);
	}
}
