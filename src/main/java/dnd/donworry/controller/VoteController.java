package dnd.donworry.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/vote")
@RequiredArgsConstructor
@Tag(name = "투표 API", description = "투표를 생성, 삭제, 조회, 수정합니다.")
@Slf4j
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
	public ResResult<VoteResponseDto> create(@RequestPart(value = "voteReuestDto") VoteRequestDto voteRequestDto,
		@RequestPart(value = "images") List<MultipartFile> images,
		@Parameter(hidden = true) Authentication authentication) {
		voteRequestDto.mapImages(images);
		return ResponseCode.VOTE_CREATED.toResponse(voteService.create(authentication.getName(), voteRequestDto));
	}

	@DeleteMapping("/{voteId}")
	@Operation(summary = "투표 삭제", description = "인증된 회원이 투표를 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "투표 삭제 성공"),
		@ApiResponse(responseCode = "400", description = "투표 삭제 실패", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"400\", \n \"message\": \"투표 삭제에 실패했습니다.\"\n}"))),
		@ApiResponse(responseCode = "404", description = "투표가 존재하지 않음", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"404\", \n \"message\": \"투표가 존재하지 않습니다.\"\n}"))),
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
	public ResResult<Void> delete(@PathVariable("voteId") Long voteId,
		@Parameter(hidden = true) Authentication authentication) {
		voteService.delete(voteId, authentication.getName());
		return ResponseCode.VOTE_DELETED.toResponse(null);
	}

	@GetMapping("/all")
	@Operation(summary = "모든 투표 조회", description = "모든 투표를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "투표 조회 성공"),
		@ApiResponse(responseCode = "400", description = "투표 조회 실패", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"400\", \n \"message\": \"투표 조회에 실패했습니다.\"\n}"))),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
	})
	public ResResult<List<VoteResponseDto>> findAllVotes() {
		return ResponseCode.VOTE_FOUND.toResponse(voteService.findAllVotes());
	}
}
