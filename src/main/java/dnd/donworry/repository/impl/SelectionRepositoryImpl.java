package dnd.donworry.repository.impl;

import static com.querydsl.jpa.JPAExpressions.*;
import static dnd.donworry.domain.entity.QSelection.*;

import java.util.List;

import dnd.donworry.domain.entity.Selection;
import dnd.donworry.repository.custom.SelectionRepositoryCustom;

public class SelectionRepositoryImpl implements SelectionRepositoryCustom {
	@Override
	public List<Selection> findByVoteId(Long voteId) {
		return selectFrom(selection)
			.where(selection.vote.id.eq(voteId))
			.fetch();
	}
}
