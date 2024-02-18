package dnd.donworry.repository.impl;

import static dnd.donworry.domain.entity.QSelection.*;

import java.util.List;
import java.util.Optional;

import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.entity.Selection;
import dnd.donworry.exception.CustomException;
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

	@Override
	public Selection findByIdCustom(Long selectionId) {
		return Optional.ofNullable(selectFrom(selection)
			.where(selection.id.eq(selectionId))
			.fetchOne()).orElseThrow(() -> new CustomException(ErrorCode.SELECTION_NOT_FOUND));
	}

	@Override
	public void deleteAllByVoteId(Long voteId) {
		delete(selection)
			.where(selection.vote.id.eq(voteId))
			.execute();
	}
}
