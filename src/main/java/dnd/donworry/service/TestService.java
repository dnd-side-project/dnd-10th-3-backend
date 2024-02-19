package dnd.donworry.service;

import dnd.donworry.domain.dto.test.TestRequestDto;
import dnd.donworry.domain.dto.test.TestResponseDto;

public interface TestService {
	TestResponseDto save(String username, TestResponseDto testResponseDto);

	TestResponseDto makeResultWithUser(String username, TestRequestDto testRequestDto);

	TestResponseDto makeResultWithOutUser(TestRequestDto testRequestDto);

	TestResponseDto findResult(String email, Long testResultId);
}
