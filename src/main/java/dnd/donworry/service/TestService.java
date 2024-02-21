package dnd.donworry.service;

import java.util.List;

import dnd.donworry.domain.dto.test.TestRequestDto;
import dnd.donworry.domain.dto.test.TestResponseDto;

public interface TestService {
	TestResponseDto saveResult(String email, TestRequestDto testRequestDto);

	List<TestResponseDto> findMyResults(String email, Long testResultId);

	TestResponseDto findResult(Long testResultId);

	void saveBackground(String email, TestResponseDto testResponseDto);
}
