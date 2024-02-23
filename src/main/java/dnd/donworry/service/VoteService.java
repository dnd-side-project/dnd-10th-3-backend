package dnd.donworry.service;

import java.util.List;

import dnd.donworry.domain.dto.vote.VoteRequestDto;
import dnd.donworry.domain.dto.vote.VoteResponseDtoWithSelection;
import dnd.donworry.domain.dto.vote.VoteUpdateDto;

public interface VoteService {

	VoteResponseDtoWithSelection create(String username, VoteRequestDto voteRequestDto);

	void delete(Long postId, String username);

	VoteResponseDtoWithSelection update(VoteUpdateDto voteUpdateDto, String email);

	List<VoteResponseDtoWithSelection> findAllVotes(String email);

	List<VoteResponseDtoWithSelection> findMyVotes(String email);

	VoteResponseDtoWithSelection findVoteDetail(Long voteId, String email);

	VoteResponseDtoWithSelection findBestVote();
}
