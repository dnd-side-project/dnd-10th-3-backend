package dnd.donworry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.donworry.domain.entity.Vote;
import dnd.donworry.repository.custom.VoteRepositoryCustom;

public interface VoteRepository extends JpaRepository<Vote, Long>, VoteRepositoryCustom {
}
