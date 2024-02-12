package dnd.donworry.service;

import java.util.List;

import dnd.donworry.domain.dto.vote.VoteRequestDto;
import dnd.donworry.domain.dto.vote.VoteResponseDto;

public interface VoteService {

	VoteResponseDto create(String username, VoteRequestDto voteRequestDto);

	void delete(Long postId, String username);

	VoteResponseDto update(Long postId, String username);

	List<VoteResponseDto> findAllVotes(Long postId);

	VoteResponseDto findVote(Long postId, String username);

}
