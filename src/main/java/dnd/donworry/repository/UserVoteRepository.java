package dnd.donworry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.donworry.domain.entity.UserVote;
import dnd.donworry.repository.custom.UserVoteRepositoryCustom;

public interface UserVoteRepository extends JpaRepository<UserVote, Long>, UserVoteRepositoryCustom {
}
