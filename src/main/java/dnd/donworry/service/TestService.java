package dnd.donworry.service;

import dnd.donworry.domain.dto.test.TestRequestDto;
import dnd.donworry.domain.dto.test.TestResponseDto;

public interface TestService {
	void save(String username, TestResponseDto testResponseDto);

	TestResponseDto makeResult(String username, TestRequestDto testRequestDto);

	TestResponseDto findResult(String email, Long testResultId);
}
