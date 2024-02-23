package dnd.donworry.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dnd.donworry.core.manager.FileManager;
import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.dto.selection.SelectionRequestDto;
import dnd.donworry.domain.dto.selection.SelectionResponseDto;
import dnd.donworry.domain.dto.vote.VoteRequestDto;
import dnd.donworry.domain.dto.vote.VoteResponseDtoWithSelection;
import dnd.donworry.domain.dto.vote.VoteUpdateDto;
import dnd.donworry.domain.entity.OptionImage;
import dnd.donworry.domain.entity.Selection;
import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.exception.CustomException;
import dnd.donworry.repository.OptionImageRepository;
import dnd.donworry.repository.SelectionRepository;
import dnd.donworry.repository.UserRepository;
import dnd.donworry.repository.UserVoteRepository;
import dnd.donworry.repository.VoteRepository;
import dnd.donworry.service.VoteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteServiceImpl implements VoteService {

	private final SelectionRepository selectionRepository;
	private final VoteRepository voteRepository;
	private final FileManager fileManager;
	private final UserRepository userRepository;
	private final UserVoteRepository userVoteRepository;
	private final OptionImageRepository optionImageRepository;

	@Override
	@Transactional
	public VoteResponseDtoWithSelection create(String email, VoteRequestDto voteRequestDto) {

		if (voteRequestDto.getSelections().size() < 2) {
			throw new CustomException(ErrorCode.SELECTION_SIZE_UNDER_TWO);
		}
		User user = userRepository.findByEmailCustom(email);
		Vote vote = voteRepository.save(Vote.toEntity(voteRequestDto, user));
		vote.setUser(user);

		List<Selection> selections = saveSelections(voteRequestDto.getSelections(), vote);

		selections.forEach(vote::addSelection);

		return VoteResponseDtoWithSelection.of(vote);
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

	public VoteResponseDtoWithSelection findVoteDetail(Long voteId, String email) {
		Vote vote = voteRepository.findByIdCustom(voteId);
		vote.addView();
		return VoteResponseDtoWithSelection.of(vote);
	}

	@Override
	@Transactional
	public VoteResponseDtoWithSelection update(VoteUpdateDto voteUpdateDto, String email) {
		Vote vote = voteRepository.findById(voteUpdateDto.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.VOTE_NOT_FOUND));

		if (!vote.getUser().getEmail().equals(email)) {
			throw new CustomException(ErrorCode.NOT_AUTHORIZED_TOKEN);
		}

		vote.update(voteUpdateDto);
		return VoteResponseDtoWithSelection.of(vote);
	}

	@Override
	public List<VoteResponseDtoWithSelection> findAllVotes(String email) {
		return voteRepository.findAllCustom();
	}

	@Override
	public List<VoteResponseDtoWithSelection> findMyVotes(String email) {
		return voteRepository.findMyVotes(email);
	}

	@Override
	public VoteResponseDtoWithSelection findBestVote() {
		return VoteResponseDtoWithSelection.of(voteRepository.findBestVote());
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

	private void setVotePercentage(List<SelectionResponseDto> selectionResponseDtos, int totalCount) {
		selectionResponseDtos.forEach(s -> s.setVotePercentage(s.getCount(), totalCount));
	}

	private List<SelectionResponseDto> findSelections(Long voteId) {
		return selectionRepository.findByVoteId(voteId).stream().map(SelectionResponseDto::of).toList();
	}

	private Long findUserSelectionForVote(String email, Long voteId) {
		return userVoteRepository.findUserSelectionForVote(email, voteId);
	}

}
