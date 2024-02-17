package dnd.donworry.repository.impl;

import static dnd.donworry.domain.entity.QSelection.*;

import java.util.List;

import dnd.donworry.domain.entity.Selection;
import dnd.donworry.repository.Support.Querydsl4RepositorySupport;
import dnd.donworry.repository.custom.SelectionRepositoryCustom;

public class SelectionRepositoryImpl extends Querydsl4RepositorySupport implements SelectionRepositoryCustom {
	public SelectionRepositoryImpl() {
		super(Selection.class);
	}

	@Override
	public List<Selection> findByVoteId(Long voteId) {
		return selectFrom(selection)
			.where(selection.vote.id.eq(voteId))
			.fetch();
	}
}
