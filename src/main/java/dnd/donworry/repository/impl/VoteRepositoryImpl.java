package dnd.donworry.repository.impl;

import static com.querydsl.jpa.JPAExpressions.*;
import static dnd.donworry.domain.entity.QVote.*;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import dnd.donworry.domain.entity.Vote;
import dnd.donworry.repository.custom.VoteRepositoryCustom;

public class VoteRepositoryImpl extends QuerydslRepositorySupport implements VoteRepositoryCustom {

	public VoteRepositoryImpl() {
		super(Vote.class);
	}

	@Override
	public List<Vote> findMyVotes(String email) {
		return selectFrom(vote).where(vote.user.email.eq(email)).fetch();
	}
}
