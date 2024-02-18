package dnd.donworry.service;

import java.util.List;

import dnd.donworry.domain.dto.vote.VoteRequestDto;
import dnd.donworry.domain.dto.vote.VoteResponseDto;
import dnd.donworry.domain.dto.vote.VoteUpdateDto;

public interface VoteService {

	VoteResponseDto create(String username, VoteRequestDto voteRequestDto);

	void delete(Long postId, String username);

	VoteResponseDto update(VoteUpdateDto voteUpdateDto, String email);

	List<VoteResponseDto> findAllVotes();

	VoteResponseDto findMyVote(String username);

}
