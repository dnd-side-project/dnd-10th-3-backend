package dnd.donworry.service.impl;

import dnd.donworry.domain.dto.test.TestResponseDto;
import dnd.donworry.domain.entity.TestResult;
import dnd.donworry.repository.TestResultRepository;
import dnd.donworry.service.TestService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final TestResultRepository testResultRepository;

    @Transactional
    public void saveAfterSignup(String username, TestResponseDto testResponseDto) {
        testResultRepository.save(TestResult.toEntity(username, testResponseDto));
    }
}
