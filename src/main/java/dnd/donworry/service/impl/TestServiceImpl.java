package dnd.donworry.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.dto.test.TestRequestDto;
import dnd.donworry.domain.dto.test.TestResponseDto;
import dnd.donworry.domain.entity.TestResult;
import dnd.donworry.domain.entity.User;
import dnd.donworry.exception.CustomException;
import dnd.donworry.manager.TestManager;
import dnd.donworry.repository.TestResultRepository;
import dnd.donworry.repository.UserRepository;
import dnd.donworry.service.TestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestServiceImpl implements TestService {

	private final TestResultRepository testResultRepository;
	private final TestManager testManager;
	private final UserRepository userRepository;

	@Transactional
	public TestResponseDto saveResult(String email, TestRequestDto testRequestDto) {
		TestResult testResult = testManager.makeResult(testRequestDto);
		if (email != null) {
			User user = userRepository.findByEmailCustom(email);
			testResult.setUser(user);
		}
		return TestResponseDto.of(testResultRepository.save(testResult));
	}

	public List<TestResponseDto> findMyResults(String email, Long testResultId) {
		User user = userRepository.findByEmailCustom(email);
		if (!user.getEmail().equals(email)) {
			throw new CustomException(ErrorCode.MEMBER_MISSMATCH);
		}
		return testResultRepository.findById(testResultId).map(TestResponseDto::of).stream().toList();
	}

	public TestResponseDto findResult(Long testResultId) {
		return testResultRepository.findById(testResultId).map(TestResponseDto::of)
			.orElseThrow(() -> new CustomException(ErrorCode.TEST_NOT_FOUND));
	}

	@Override
	public void saveBackground(String email, TestResponseDto testResponseDto) {
		TestResult testResult = testResultRepository.findById(testResponseDto.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.TEST_NOT_FOUND));

		testResult.setUser(userRepository.findByEmailCustom(email));

		testResultRepository.save(testResult);
	}

}
