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
import dnd.donworry.domain.dto.vote.VoteResponseDto;
import dnd.donworry.domain.dto.vote.VoteUpdateDto;
import dnd.donworry.domain.entity.OptionImage;
import dnd.donworry.domain.entity.Selection;
import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.exception.CustomException;
import dnd.donworry.repository.OptionImageRepository;
import dnd.donworry.repository.SelectionRepository;
import dnd.donworry.repository.UserRepository;
import dnd.donworry.repository.VoteRepository;
import dnd.donworry.service.VoteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

	private final SelectionRepository selectionRepository;
	private final OptionImageRepository optionImageRepository;
	private final VoteRepository voteRepository;
	private final FileManager fileManager;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public VoteResponseDto create(String email, VoteRequestDto voteRequestDto) {

		if (voteRequestDto.getSelections().size() < 2) {
			throw new CustomException(ErrorCode.SELECTION_SIZE_UNDER_TWO);
		}
		User user = userRepository.findByEmailCustom(email);
		Vote vote = voteRepository.save(Vote.toEntity(voteRequestDto, user));

		List<SelectionResponseDto> selectionResponseDtos = saveSelections(voteRequestDto.getSelections(), vote);
		setVotePercentage(selectionResponseDtos, vote.getVoters());

		return VoteResponseDto.of(vote, selectionResponseDtos);
	}

	@Override
	@Transactional
	public void delete(Long voteId, String email) {
		Vote vote = voteRepository.findById(voteId).orElseThrow(() -> new CustomException(ErrorCode.VOTE_NOT_FOUND));
		if (!vote.getUser().getEmail().equals(email)) {
			throw new CustomException(ErrorCode.NOT_AUTHORIZED_TOKEN);
		}
		voteRepository.delete(vote);
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
		return VoteResponseDto.of(vote, findSelections(vote.getId()));
	}

	@Override
	public List<VoteResponseDto> findAllVotes() {
		return voteRepository.findAll().stream().map(v -> VoteResponseDto.of(v, findSelections(v.getId()))).toList();
	}

	@Override
	public VoteResponseDto findMyVote(String email) {
		return null;
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
	protected List<SelectionResponseDto> saveSelections(List<SelectionRequestDto> selections, Vote vote) {
		List<SelectionResponseDto> selectionResponseDtos = new ArrayList<>();
		selections.forEach(s -> {
			Selection selection =
				s.getImage() != null
					? createSelectionWithImage(s, vote)
					: createSelectionWithoutImage(s, vote);
			selectionResponseDtos.add(SelectionResponseDto.of(selectionRepository.save(selection)));
		});
		return selectionResponseDtos;
	}

	@Transactional
	protected Selection createSelectionWithImage(SelectionRequestDto selectionRequestDto, Vote vote) {
		Selection selection = Selection.toEntity(selectionRequestDto.getContent(), vote);
		String imagePath = saveImage(selectionRequestDto.getImage());
		OptionImage optionImage = OptionImage.toEntity(imagePath, selection);
		return selection.setOptionImage(optionImage);
	}

	@Transactional
	private Selection createSelectionWithoutImage(SelectionRequestDto selectionRequestDto, Vote vote) {
		return Selection.toEntity(selectionRequestDto.getContent(), vote);
	}

	private void setVotePercentage(List<SelectionResponseDto> selectionResponseDtos, int totalCount) {
		selectionResponseDtos.forEach(s -> s.setVotePercentage(s.getCount(), totalCount));
	}

	private List<SelectionResponseDto> findSelections(Long voteId) {
		return selectionRepository.findByVoteId(voteId).stream().map(SelectionResponseDto::of).toList();
	}
}
