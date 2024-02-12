package dnd.donworry.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dnd.donworry.domain.constants.ResResult;
import dnd.donworry.domain.constants.ResponseCode;
import dnd.donworry.domain.dto.vote.VoteRequestDto;
import dnd.donworry.domain.dto.vote.VoteResponseDto;
import dnd.donworry.service.VoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/vote")
@RequiredArgsConstructor
@Tag(name = "투표 API", description = "투표를 생성, 삭제, 조회, 수정합니다.")
public class VoteController {

	private final VoteService voteService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResResult<VoteResponseDto> create(@RequestPart VoteRequestDto voteRequestDto,
		@RequestPart List<MultipartFile> images) {
		String username = "test"; // Authentication.getName()으로 변경
		voteRequestDto.mapImages(images);
		return ResponseCode.VOTE_CREATED.toResponse(voteService.create(username, voteRequestDto));
	}
}
