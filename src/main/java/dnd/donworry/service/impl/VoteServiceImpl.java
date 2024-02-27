package dnd.donworry.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dnd.donworry.core.manager.FileManager;
import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.dto.selection.SelectionRequestDto;
import dnd.donworry.domain.dto.selection.SelectionResponseDto;
import dnd.donworry.domain.dto.vote.VoteRequestDto;
import dnd.donworry.domain.dto.vote.VoteResponseDto;
import dnd.donworry.domain.dto.vote.VoteUpdateDto;
import dnd.donworry.domain.entity.OptionImage;
import dnd.donworry.domain.entity.Selection;
import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.entity.UserVote;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.domain.entity.VoteLike;
import dnd.donworry.exception.CustomException;
import dnd.donworry.repository.OptionImageRepository;
import dnd.donworry.repository.SelectionRepository;
import dnd.donworry.repository.UserRepository;
import dnd.donworry.repository.UserVoteRepository;
import dnd.donworry.repository.VoteLikeRepository;
import dnd.donworry.repository.VoteRepository;
import dnd.donworry.service.VoteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteServiceImpl implements VoteService {
	private final VoteLikeRepository voteLikeRepository;

	private final SelectionRepository selectionRepository;
	private final VoteRepository voteRepository;
	private final FileManager fileManager;
	private final UserRepository userRepository;
	private final UserVoteRepository userVoteRepository;
	private final OptionImageRepository optionImageRepository;

	@Override
	@Transactional
	public VoteResponseDto create(String email, VoteRequestDto voteRequestDto) {

		if (voteRequestDto.getSelections().size() < 2) {
			throw new CustomException(ErrorCode.SELECTION_SIZE_UNDER_TWO);
		}
		User user = userRepository.findByEmailCustom(email);
		Vote vote = voteRepository.save(Vote.toEntity(voteRequestDto, user));
		vote.setUser(user);

		List<Selection> selections = saveSelections(voteRequestDto.getSelections(), vote);

		selections.forEach(vote::addSelection);

		return VoteResponseDto.of(vote);
	}

	@Override
	@Transactional
	public void delete(Long voteId, String email) {
		Vote vote = voteRepository.findById(voteId).orElseThrow(() -> new CustomException(ErrorCode.VOTE_NOT_FOUND));
		if (!vote.getUser().getEmail().equals(email)) {
			throw new CustomException(ErrorCode.NOT_AUTHORIZED_TOKEN);
		}
		optionImageRepository.deleteAllByVoteId(voteId);
		selectionRepository.deleteAllByVoteId(voteId);
		voteRepository.delete(vote);
	}

	@Transactional
	public VoteResponseDto findVoteDetail(Long voteId, String email) {
		Vote vote = voteRepository.findByIdCustom(voteId);
		vote.addView();
		VoteResponseDto voteResponseDto = setUserSelection(vote, email);
		voteResponseDto.getSelections().forEach(s -> s.setVotePercentage(s.getCount(), voteResponseDto.getVoters()));

		return voteResponseDto;
	}

	@Override
	@Transactional
	public VoteResponseDto update(VoteUpdateDto voteUpdateDto, String email) {
		Vote vote = voteRepository.findById(voteUpdateDto.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.VOTE_NOT_FOUND));

		if (!vote.getUser().getEmail().equals(email)) {
			throw new CustomException(ErrorCode.NOT_AUTHORIZED_TOKEN);
		}

		vote.update(voteUpdateDto);

		VoteResponseDto votes = setUserSelection(vote, email);

		votes.getSelections().forEach(s -> s.setVotePercentage(s.getCount(), votes.getVoters()));
		return votes;
	}

	@Override
	public List<VoteResponseDto> findAllVotes(String email) {
		if (email == null) {
			return voteRepository.findAllCustom().stream().map(VoteResponseDto::of).toList();
		}
		List<VoteResponseDto> votes = voteRepository.findAllCustom()
			.stream()
			.map(v -> setUserSelection(v, email))
			.toList();

		setVotePercentage(votes);

		return votes;
	}

	@Override
	public List<VoteResponseDto> findMyVotes(String email) {
		List<VoteResponseDto> votes = voteRepository.findMyVotes(email)
			.stream()
			.map(v -> setUserSelection(v, email))
			.toList();
		setVotePercentage(votes);
		return votes;
	}

	@Override
	public VoteResponseDto findBestVote() {
		VoteResponseDto bestVote = VoteResponseDto.of(voteRepository.findBestVote());
		bestVote.getSelections().forEach(s -> s.setVotePercentage(s.getCount(), bestVote.getVoters()));
		return bestVote;
	}

	@Override
	@Transactional
	public Boolean updateLikes(Long voteId, String name) {
		Vote vote = voteRepository.findById(voteId).orElseThrow(() -> new CustomException(ErrorCode.VOTE_NOT_FOUND));
		User user = userRepository.findByEmailCustom(name);
		Optional<VoteLike> voteLikeOp = voteLikeRepository.findByVoteAndUserCustom(vote, user);
		if (voteLikeOp.isPresent()) {
			VoteLike voteLike = voteLikeOp.get();
			if (voteLike.isStatus()) {
				voteLike.updateStatus();
				vote.minusLike();
				return false;
			} else {
				voteLike.updateStatus();
				vote.addLike();
				return true;
			}
		}
		voteLikeRepository.save(VoteLike.toEntity(vote, user));
		vote.addLike();
		return true;
	}

	@Transactional
	protected String saveImage(MultipartFile image) {
		try {
			return fileManager.upload("Selection", image);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAIL);
		}
	}

	@Transactional
	protected List<Selection> saveSelections(List<SelectionRequestDto> selections, Vote vote) {
		List<Selection> selectionList = new ArrayList<>();
		selections.forEach(s -> selectionList.add(selectionRepository.save(s.getImage() != null
			? createSelectionWithImage(s, vote)
			: createSelectionWithoutImage(s, vote))));
		return selectionList;
	}

	@Transactional
	protected Selection createSelectionWithImage(SelectionRequestDto selectionRequestDto, Vote vote) {
		Selection selection = Selection.toEntity(selectionRequestDto.getContent(), vote);
		String imagePath = saveImage(selectionRequestDto.getImage());
		OptionImage optionImage = OptionImage.toEntity(imagePath, selection);
		return selection.setOptionImage(optionImage);
	}

	@Transactional
	protected Selection createSelectionWithoutImage(SelectionRequestDto selectionRequestDto, Vote vote) {
		return Selection.toEntity(selectionRequestDto.getContent(), vote);
	}

	private void setVotePercentage(List<VoteResponseDto> votes) {
		votes.forEach(v -> v.getSelections().forEach(s -> s.setVotePercentage(s.getCount(), v.getVoters())));
	}

	private List<SelectionResponseDto> findSelections(Long voteId) {
		return selectionRepository.findByVoteId(voteId).stream().map(SelectionResponseDto::of).toList();
	}

	private Long findUserSelectionForVote(String email, Long voteId) {
		return userVoteRepository.findUserSelectionForVote(email, voteId);
	}

	private VoteResponseDto setUserSelection(Vote vote, String email) {
		Optional<UserVote> userVote = userVoteRepository.findUserVoteByEmailAndVoteId(email, vote.getId());

		VoteResponseDto voteResponseDto = VoteResponseDto.of(vote);
		userVote.ifPresent(value -> voteResponseDto.setSelected(value.getSelection().getId()));

		return voteResponseDto;
	}
}
