package dnd.donworry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.donworry.domain.entity.VoteLike;
import dnd.donworry.repository.custom.VoteLikeRepositoryCustom;

public interface VoteLikeRepository extends JpaRepository<VoteLike, Long>, VoteLikeRepositoryCustom {
}