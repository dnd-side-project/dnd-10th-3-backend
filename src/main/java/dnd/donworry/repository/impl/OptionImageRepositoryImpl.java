package dnd.donworry.repository.impl;

import static dnd.donworry.domain.entity.QOptionImage.*;

import dnd.donworry.domain.entity.OptionImage;
import dnd.donworry.repository.Support.Querydsl4RepositorySupport;
import dnd.donworry.repository.custom.OptionImageRepositoryCustom;

public class OptionImageRepositoryImpl extends Querydsl4RepositorySupport implements OptionImageRepositoryCustom {
	public OptionImageRepositoryImpl() {
		super(OptionImage.class);
	}

	@Override
	public void deleteAllByVoteId(Long voteId) {
		delete(optionImage)
			.where(optionImage.selection.vote.id.eq(voteId))
			.execute();
	}
}
