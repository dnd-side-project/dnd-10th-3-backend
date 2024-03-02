package dnd.donworry.repository.impl;

import static dnd.donworry.domain.entity.QOptionImage.*;
import static dnd.donworry.domain.entity.QSelection.*;
import static dnd.donworry.domain.entity.QUser.*;
import static dnd.donworry.domain.entity.QVote.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
		return selectFrom(vote)
			.leftJoin(vote.user, user).fetchJoin()
			.leftJoin(vote.selections, selection).fetchJoin()
			.leftJoin(selection.optionImage, optionImage).fetchJoin()
			.where(vote.user.email.eq(email))
			.orderBy(vote.views.desc())
			.fetch();
	}

	@Override
	public Vote findBestVote() {
		return Optional.ofNullable(
			selectFrom(vote)
				.leftJoin(vote.user, user).fetchJoin()
				.leftJoin(vote.selections, selection).fetchJoin()
				.leftJoin(selection.optionImage, optionImage).fetchJoin()
				.orderBy(vote.views.desc())
				.fetchFirst()
		).orElseThrow(() -> new CustomException(ErrorCode.VOTE_NOT_FOUND));
	}

	@Override
	public Vote findByIdCustom(Long voteId) {
		return Optional.ofNullable(selectFrom(vote).where(vote.id.eq(voteId))
				.leftJoin(vote.user, user).fetchJoin()
				.leftJoin(vote.selections, selection).fetchJoin()
				.leftJoin(selection.optionImage, optionImage).fetchJoin()
				.fetchFirst())
			.orElseThrow(() -> new CustomException(ErrorCode.VOTE_NOT_FOUND));
	}

	@Override
	public List<Vote> findAllCustom() {
		return selectFrom(vote)
			.leftJoin(vote.user, user).fetchJoin()
			.leftJoin(vote.selections, selection).fetchJoin()
			.leftJoin(selection.optionImage, optionImage).fetchJoin()
			.orderBy(vote.views.desc())
			.fetch();
	}

	@Override
	public Page<Vote> searchVotes(String keyword, Pageable pageable) {
		List<Vote> votes = selectFrom(vote)
			.leftJoin(vote.user, user).fetchJoin()
			.leftJoin(vote.selections, selection).fetchJoin()
			.leftJoin(selection.optionImage, optionImage).fetchJoin()
			.where(vote.title.contains(keyword).or(vote.content.contains(keyword)))
			.orderBy(vote.views.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = select(vote.id.count())
			.from(vote)
			.where(vote.title.contains(keyword).or(vote.content.contains(keyword)))
			.fetchOne();

		long totalCount = (total != null) ? total : 0L;

		return new PageImpl<>(votes, pageable, totalCount);
	}

}
