package dnd.donworry.repository.custom;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dnd.donworry.domain.entity.Vote;

public interface VoteRepositoryCustom {

	List<Vote> findMyVotes(String email);

	Vote findBestVote();

	Vote findByIdCustom(Long voteId);

	List<Vote> findAllCustom();

	Page<Vote> searchVotes(String keyword, Pageable pageable);

}
