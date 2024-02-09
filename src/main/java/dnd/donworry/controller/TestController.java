package dnd.donworry.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnd.donworry.domain.constants.ResponseCode;
import dnd.donworry.domain.dto.test.TestRequestDto;
import dnd.donworry.domain.dto.test.TestResponseDto;
import dnd.donworry.service.TestService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

	private final TestService testService;

	@PostMapping("/background")
	public ResponseEntity<?> save(@RequestBody TestResponseDto testResponseDto) {
		String username = "test"; // Authentication.getName()으로 변경
		testService.save(username, testResponseDto);
		return ResponseCode.TEST_SAVED.toResponse(null);
	}

	@PostMapping
	public ResponseEntity<?> makeResult(@RequestBody TestRequestDto testRequestDto) {
		String username = "test"; // Authentication.getName()으로 변경
		return ResponseCode.TEST_SUCCESS.toResponse(testService.makeResult(username, testRequestDto));
	}

	@GetMapping("/result/{resultId}")
	public ResponseEntity<?> findResult(@PathVariable Long resultId) {
		return ResponseCode.TEST_SUCCESS.toResponse(testService.findResult(resultId));
	}
}
