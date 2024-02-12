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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/vote")
@RequiredArgsConstructor
@Tag(name = "투표 API", description = "투표를 생성, 삭제, 조회, 수정합니다.")
public class VoteController {

	private final VoteService voteService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "투표 생성", description = "인증된 회원이 투표를 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "투표 생성 성공"),
		@ApiResponse(responseCode = "400", description = "투표 생성 실패", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"400\", \n \"message\": \"투표 생성에 실패했습니다.\"\n}"))),
		@ApiResponse(responseCode = "404", description = "투표 생성 실패", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"404\", \n \"message\": \"입력값이 잘못되었습니다.\"\n}"))),
		@ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"403\", \n \"message\": \"접근 권한이 없습니다.\"\n}"))),
		@ApiResponse(responseCode = "401", description = "토큰이 존재하지 않음", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"401\", \n \"message\": \"유효한 토큰이 존재하지 않습니다.\"\n}"))),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
	})
	public ResResult<VoteResponseDto> create(@RequestPart VoteRequestDto voteRequestDto,
		@RequestPart List<MultipartFile> images) {
		String username = "test"; // Authentication.getName()으로 변경
		voteRequestDto.mapImages(images);
		return ResponseCode.VOTE_CREATED.toResponse(voteService.create(username, voteRequestDto));
	}
}
