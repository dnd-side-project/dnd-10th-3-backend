package dnd.donworry.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnd.donworry.domain.constants.ResResult;
import dnd.donworry.domain.constants.ResponseCode;
import dnd.donworry.domain.dto.test.TestRequestDto;
import dnd.donworry.domain.dto.test.TestResponseDto;
import dnd.donworry.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Tag(name = "테스트 API", description = "테스트 결과를 생성하고 조회합니다.")
public class TestController {

	private final TestService testService;

	@PostMapping("/background")
	@Operation(summary = "백그라운드 테스트 결과 저장", description = "비회원이 테스트 후 회원가입을 진행할 시 백그라운드로 테스트 결과를 저장합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "테스트 결과 저장 성공", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"200\", \n \"message\": \"테스트 결과 저장에 성공했습니다.\"\n}"))),
		@ApiResponse(responseCode = "400", description = "테스트 결과 저장 실패", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"400\", \n \"message\": \"테스트 결과 저장에 실패했습니다.\"\n}"))),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
	})
	public ResResult<?> save(@RequestBody TestResponseDto testResponseDto,
		@Parameter(hidden = true) Authentication authentication) {
		testService.save(authentication.getName(), testResponseDto);
		return ResponseCode.TEST_SUCCESS.toResponse(null);
	}

	@PostMapping("/result")
	@Operation(summary = "테스트 결과 생성", description = "일반적인 테스트 진행 후 결과를 생성합니다. 비회원인 경우 백그라운드에 저장되며 회원가입 시 저장된 결과를 불러옵니다. 회원인 경우에는 바로 결과가 저장됩니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "테스트 결과 생성 성공"),
		@ApiResponse(responseCode = "400", description = "테스트 결과 생성 실패", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"400\", \n \"message\": \"테스트 결과 조회에 실패했습니다.\"\n}"))),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
	})
	public ResResult<TestResponseDto> makeResult(@RequestBody TestRequestDto testRequestDto,
		@Parameter(hidden = true) Authentication authentication) {
		return authentication == null
			? ResponseCode.TEST_SUCCESS.toResponse(testService.makeResult(null, testRequestDto))
			: ResponseCode.TEST_SUCCESS.toResponse(testService.makeResult(authentication.getName(), testRequestDto));
	}

	@GetMapping("/result/{resultId}")
	@Operation(summary = "테스트 결과 조회", description = "테스트 결과를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "테스트 결과 조회 성공"),
		@ApiResponse(responseCode = "400", description = "테스트 결과 조회 실패", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"400\", \n \"message\": \"테스트 결과 조회에 실패했습니다. \"\n}"))),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}"))),
		@ApiResponse(responseCode = "404", description = "테스트 결과 없음", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"404\", \n \"message\": \"해당 테스트 결과가 존재하지 않습니다.\"\n}"))),
		@ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"403\", \n \"message\": \"접근 권한이 없습니다.\"\n}"))),
		@ApiResponse(responseCode = "401", description = "토큰이 존재하지 않음", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"401\", \n \"message\": \"유효한 토큰이 존재하지 않습니다.\"\n}")))
	})
	public ResResult<TestResponseDto> findResult(@PathVariable("resultId") Long resultId,
		@Parameter(hidden = true) Authentication authentication) {
		log.info("email: {}", authentication.getName());
		return ResponseCode.TEST_SUCCESS.toResponse(testService.findResult(authentication.getName(), resultId));
	}
}
