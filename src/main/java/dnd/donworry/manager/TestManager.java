package dnd.donworry.manager;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import dnd.donworry.domain.constants.RANK;
import dnd.donworry.domain.dto.test.TestRequestDto;
import dnd.donworry.domain.entity.TestResult;
import dnd.donworry.repository.TestResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestManager {

	public static final String LOW = "low";
	public static final String MID = "mid";
	public static final String HIGH = "high";
	public static final int LOW_TEMPERATURE = 1;
	public static final int MID_TEMPERATURE = 2;
	public static final int HIGH_TEMPERATURE = 3;
	public static final int HIGHEST_TEMPERATURE = 4;
	private final TestResultRepository testResultRepository;

	public TestResult makeResult(TestRequestDto testRequestDto) {
		log.info(Arrays.toString(testRequestDto.factorList()));
		return TestResult.toEntity(null, testRequestDto, RANK.findByTemperature(calculateTemperature(testRequestDto)));
	}

	// 리팩토링 필요 (상수 -> 변수)
	private String calculateFactor(Long factor) {
		return (factor < -1) ? LOW
			: (factor < 2) ? MID
			: HIGH;
	}

	private int calculateTemperature(TestRequestDto testRequestDto) {
		int low = 0, mid = 0, high = 0;

		for (Long factor : testRequestDto.factorList()) {
			String factorType = calculateFactor(factor);
			if (factorType.equals(LOW))
				low++;
			else if (factorType.equals(MID))
				mid++;
			else
				high++;
		}

		return high >= 3 ? HIGHEST_TEMPERATURE
			: high == 2 ? HIGH_TEMPERATURE
			: mid >= 2 || (high == 1 & mid == 1 & low == 1) ? MID_TEMPERATURE
			: LOW_TEMPERATURE;
	}

	private String makeImageUrl(int temperature) { // url 수정 필요
		return "https://s3.ap-northeast-2.amazonaws.com/donworry/" + temperature + ".png";
	}

}
