package dnd.donworry.service.impl;

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
	@Override
	public TestResponseDto makeResultWithUser(String email, TestRequestDto testRequestDto) {
		TestResponseDto testReponseDto = testManager.makeResult(testRequestDto);
		save(email, testReponseDto);
		return testReponseDto;
	}

	@Transactional
	@Override
	public TestResponseDto makeResultWithOutUser(TestRequestDto testRequestDto) {
		TestResponseDto testReponseDto = testManager.makeResult(testRequestDto);
		return testReponseDto;
	}

	@Override
	public TestResponseDto findResult(String email, Long testResultId) {
		User user = userRepository.findByEmailCustom(email);
		if (!user.getEmail().equals(email)) {
			throw new CustomException(ErrorCode.MEMBER_MISSMATCH);
		}
		return testResultRepository.findById(testResultId).map(TestResponseDto::of)
			.orElseThrow(() -> new CustomException(ErrorCode.TEST_NOT_FOUND));
	}

	@Transactional
	public void save(String nickname, TestResponseDto testResponseDto) {
		User user = userRepository.findByEmailCustom(nickname);
		testResultRepository.save(TestResult.toEntity(user, testResponseDto));
	}

}
