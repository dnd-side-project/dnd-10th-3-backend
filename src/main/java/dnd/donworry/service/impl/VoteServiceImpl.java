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
	public VoteResponseDto create(String nickname, VoteRequestDto voteRequestDto) {

		if (voteRequestDto.getSelections().size() < 2) {
			throw new CustomException(ErrorCode.SELECTION_SIZE_UNDER_TWO);
		}
		User user = userRepository.findByNickname(nickname);
		Vote vote = voteRepository.save(Vote.toEntity(voteRequestDto, user));

		List<SelectionResponseDto> selectionResponseDtos = saveSelections(voteRequestDto.getSelections(), vote);
		setVotePercentage(selectionResponseDtos, vote.getVoters());

		return VoteResponseDto.of(vote, selectionResponseDtos);
	}

	@Override
	public void delete(Long postId, String username) {

	}

	@Override
	public VoteResponseDto update(Long postId, String username) {
		return null;
	}

	@Override
	public List<VoteResponseDto> findAllVotes(Long postId) {
		return null;
	}

	@Override
	public VoteResponseDto findVote(Long postId, String username) {
		return null;
	}

	private String saveImage(MultipartFile image) {
		try {
			return fileManager.upload("Selection", image);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAIL);
		}
	}

	private List<SelectionResponseDto> saveSelections(List<SelectionRequestDto> selections, Vote vote) {
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

	private Selection createSelectionWithImage(SelectionRequestDto selectionRequestDto, Vote vote) {
		Selection selection = Selection.toEntity(vote);
		String imagePath = saveImage(selectionRequestDto.getImage());
		OptionImage optionImage = OptionImage.toEntity(imagePath, selection);
		selection.setOptionImage(optionImage);
		optionImageRepository.save(optionImage);
		return selection;
	}

	private Selection createSelectionWithoutImage(SelectionRequestDto selectionRequestDto, Vote vote) {
		return Selection.toEntity(selectionRequestDto.getContent(), vote);
	}

	private void setVotePercentage(List<SelectionResponseDto> selectionResponseDtos, int totalCount) {
		selectionResponseDtos.forEach(s -> s.setVotePercentage(s.getCount(), totalCount));
	}
}
