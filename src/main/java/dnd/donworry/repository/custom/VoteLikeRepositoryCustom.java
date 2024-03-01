package dnd.donworry.repository.custom;

import java.util.Optional;

import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.domain.entity.VoteLike;

public interface VoteLikeRepositoryCustom {
	Optional<VoteLike> findByVoteAndUserCustom(Vote vote, User user);
}
