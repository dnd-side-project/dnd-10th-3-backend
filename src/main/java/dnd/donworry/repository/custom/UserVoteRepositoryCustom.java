package dnd.donworry.repository.custom;

import java.util.Optional;

import dnd.donworry.domain.entity.UserVote;

public interface UserVoteRepositoryCustom {

	Optional<UserVote> findUserVoteByEmailAndVoteId(String email, Long voteId);
}
