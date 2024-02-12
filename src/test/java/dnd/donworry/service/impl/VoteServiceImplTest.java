package dnd.donworry.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import dnd.donworry.core.manager.FileManager;
import dnd.donworry.domain.dto.selection.SelectionRequestDto;
import dnd.donworry.domain.dto.vote.VoteRequestDto;
import dnd.donworry.domain.entity.Selection;
import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.exception.CustomException;
import dnd.donworry.repository.OptionImageRepository;
import dnd.donworry.repository.SelectionRepository;
import dnd.donworry.repository.VoteRepository;
import jakarta.transaction.Transactional;

@ExtendWith(MockitoExtension.class)
class VoteServiceImplTest {

	@Mock
	OptionImageRepository optionImageRepository;
	@Mock
	private VoteRepository voteRepository;
	@Mock
	private SelectionRepository selectionRepository;
	@Mock
	private FileManager fileManager;

	@InjectMocks
	private VoteServiceImpl voteServiceImpl;

	@Test
	@DisplayName("투표는 최소 2개의 선택지를 가져야 한다.")
	@Transactional
	void create_under_two_selections() {
		// Given
		VoteRequestDto voteRequestDto = new VoteRequestDto();
		voteRequestDto.setSelections(List.of(new SelectionRequestDto("test1", null)));

		// When

		// Then
		assertThrows(CustomException.class, () -> voteServiceImpl.create("test", voteRequestDto));
	}

	@Test
	@DisplayName("이미지가 없는 투표 생성이 가능하다.")
	@Transactional
	void create_over_two_selections_with_content() {
		// Given
		VoteRequestDto voteRequestDto = new VoteRequestDto();
		voteRequestDto.setSelections(
			List.of(new SelectionRequestDto("test1", null), new SelectionRequestDto("test2", null)));
		User user = new User(1L, "test", "test", "");

		// When
		when(voteRepository.save(any())).thenReturn(Vote.toEntity(voteRequestDto, user));
		when(selectionRepository.save(any())).thenReturn(
			new Selection(1L, Vote.toEntity(voteRequestDto, user), null, "testContent", 0));

		// Then
		assertDoesNotThrow(() -> voteServiceImpl.create("test", voteRequestDto));
	}

	@Test
	@DisplayName("이미지가 있는 투표 생성이 가능하다.")
	@Transactional
	void create_over_two_selections_with_image() throws Exception {
		// Given
		VoteRequestDto voteRequestDto = new VoteRequestDto();
		voteRequestDto.setSelections(
			List.of(new SelectionRequestDto(null, new MockMultipartFile("test1", new byte[] {})),
				new SelectionRequestDto(null, new MockMultipartFile("test2", new byte[] {}))));
		User user = new User(1L, "test", "test", "");

		// When
		when(voteRepository.save(any())).thenReturn(Vote.toEntity(voteRequestDto, user));
		when(selectionRepository.save(any())).thenReturn(
			new Selection(1L, Vote.toEntity(voteRequestDto, user), null, "testContent", 0));
		when(fileManager.upload(anyString(), any())).thenReturn("test");

		// Then
		assertDoesNotThrow(() -> voteServiceImpl.create("test", voteRequestDto));
	}
}