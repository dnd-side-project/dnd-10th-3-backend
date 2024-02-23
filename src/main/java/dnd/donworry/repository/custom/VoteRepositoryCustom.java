package dnd.donworry.repository.custom;

import java.util.List;

import dnd.donworry.domain.dto.vote.VoteResponseDtoWithSelection;
import dnd.donworry.domain.entity.Vote;

public interface VoteRepositoryCustom {

	List<VoteResponseDtoWithSelection> findMyVotes(String email);

	Vote findBestVote();

	Vote findByIdCustom(Long voteId);

	List<VoteResponseDtoWithSelection> findAllCustom();

}
