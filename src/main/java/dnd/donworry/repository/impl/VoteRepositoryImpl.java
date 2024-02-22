package dnd.donworry.repository.impl;

import static dnd.donworry.domain.entity.QVote.*;

import java.util.List;
import java.util.Optional;

import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.exception.CustomException;
import dnd.donworry.repository.Support.Querydsl4RepositorySupport;
import dnd.donworry.repository.custom.VoteRepositoryCustom;

public class VoteRepositoryImpl extends Querydsl4RepositorySupport implements VoteRepositoryCustom {

	public VoteRepositoryImpl() {
		super(Vote.class);
	}

	@Override
	public List<Vote> findMyVotes(String email) {
		return selectFrom(vote).where(vote.user.email.eq(email)).fetch();
	}

	@Override
	public Vote findBestVote() {
		return Optional.ofNullable(
			selectFrom(vote)
				.orderBy(vote.views.desc())
				.fetchFirst()
		).orElseThrow(() -> new CustomException(ErrorCode.VOTE_NOT_FOUND));
	}

	@Override
	public Vote findByIdCustom(Long voteId) {
		return Optional.ofNullable(selectFrom(vote).where(vote.id.eq(voteId)).fetchFirst())
			.orElseThrow(() -> new CustomException(ErrorCode.VOTE_NOT_FOUND));
	}

}
