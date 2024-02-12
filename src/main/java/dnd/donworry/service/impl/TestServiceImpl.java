package dnd.donworry.service.impl;

import org.springframework.stereotype.Service;

import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.dto.test.TestRequestDto;
import dnd.donworry.domain.dto.test.TestResponseDto;
import dnd.donworry.domain.entity.TestResult;
import dnd.donworry.exception.CustomException;
import dnd.donworry.manager.TestManager;
import dnd.donworry.repository.TestResultRepository;
import dnd.donworry.service.TestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

	private final TestResultRepository testResultRepository;
	private final TestManager testManager;

	@Transactional
	@Override
	public TestResponseDto makeResult(String username, TestRequestDto testRequestDto) {
		TestResponseDto testReponseDto = testManager.makeResult(testRequestDto);
		if (username != null)
			save(username, testReponseDto);
		return testReponseDto;
	}

	@Override
	public TestResponseDto findResult(Long testResultId) {
		return testResultRepository.findById(testResultId).map(TestResponseDto::of).orElseThrow(
			() -> new CustomException(ErrorCode.TEST_NOT_FOUND));
	}

	@Transactional
	public void save(String username, TestResponseDto testResponseDto) {
		testResultRepository.save(TestResult.toEntity(username, testResponseDto));
	}

}
