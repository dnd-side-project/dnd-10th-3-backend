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

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

	private final TestResultRepository testResultRepository;
	private final TestManager testManager;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public TestResponseDto makeResult(String nickname, TestRequestDto testRequestDto) {
		TestResponseDto testReponseDto = testManager.makeResult(testRequestDto);
		if (nickname != null)
			save(nickname, testReponseDto);
		return testReponseDto;
	}

	@Override
	public TestResponseDto findResult(Long testResultId) {
		return testResultRepository.findById(testResultId).map(TestResponseDto::of).orElseThrow(
			() -> new CustomException(ErrorCode.TEST_NOT_FOUND));
	}

	@Transactional
	public void save(String nickname, TestResponseDto testResponseDto) {
		User user = userRepository.findByNickname(nickname);
		testResultRepository.save(TestResult.toEntity(user, testResponseDto));
	}

}
