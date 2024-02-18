package dnd.donworry.repository.custom;

import java.util.List;

import dnd.donworry.domain.entity.Selection;

public interface SelectionRepositoryCustom {

	List<Selection> findByVoteId(Long voteId);

	Selection findByIdCustom(Long selectionId);
}
