package dnd.donworry.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import dnd.donworry.domain.dto.vote.VotePagingDto;
import dnd.donworry.domain.dto.vote.VoteRequestDto;
import dnd.donworry.domain.dto.vote.VoteResponseDto;
import dnd.donworry.domain.dto.vote.VoteUpdateDto;

public interface VoteService {

	VoteResponseDto create(String username, VoteRequestDto voteRequestDto);

	void delete(Long postId, String username);

	VoteResponseDto update(VoteUpdateDto voteUpdateDto, String email);

	List<VoteResponseDto> findAllVotes(String email);

	List<VoteResponseDto> findMyVotes(String email);

	VoteResponseDto findVoteDetail(Long voteId, String email);

	VoteResponseDto findBestVote();

	Boolean updateLikes(Long voteId, String email);

	VotePagingDto searchVotes(String keyword, String email, Pageable pageable);
}
