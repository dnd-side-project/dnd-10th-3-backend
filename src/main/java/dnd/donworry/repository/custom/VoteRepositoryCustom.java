package dnd.donworry.repository.custom;

import java.util.List;

import dnd.donworry.domain.entity.Vote;

public interface VoteRepositoryCustom {

	List<Vote> findMyVotes(String email);

	Vote findBestVote();

	Vote findByIdCustom(Long voteId);

}
