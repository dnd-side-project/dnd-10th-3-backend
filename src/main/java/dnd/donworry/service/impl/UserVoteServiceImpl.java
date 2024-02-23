package dnd.donworry.service.impl;

import org.springframework.stereotype.Service;

import dnd.donworry.domain.entity.UserVote;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.repository.SelectionRepository;
import dnd.donworry.repository.UserRepository;
import dnd.donworry.repository.UserVoteRepository;
import dnd.donworry.service.UserVoteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserVoteServiceImpl implements UserVoteService {

	private final UserVoteRepository userVoteRepository;
	private final SelectionRepository selectionRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public void attend(String email, Long voteId, Long selectionId) {
		Vote vote = selectionRepository.findByIdCustom(selectionId).getVote();

		// 이미 참여한 경우 (같은 선택지 -> 취소, 다른 선택지 -> 변경)
		userVoteRepository.findUserVoteByEmailAndVoteId(email, voteId).ifPresent(uv -> {
			vote.minusVoter();
			userVoteRepository.delete(uv);
		});

		vote.addVoter();
		if (selectionId != null) {
			userVoteRepository.save(
				UserVote.of(userRepository.findByEmailCustom(email), selectionRepository.findByIdCustom(selectionId)));
		}

	}

}
