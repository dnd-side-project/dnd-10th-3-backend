package dnd.donworry.service;

public interface UserVoteService {

	void attend(String email, Long voteId, Long selectionId);
	
}
