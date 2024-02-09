package dnd.donworry.domain.dto.test;

import java.time.LocalDateTime;

import dnd.donworry.domain.entity.TestResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestResponseDto {
	private String buddy;
	private Long trust;
	private Long love;
	private Long talk;
	private int temperature;
	private String imageUrl;
	private LocalDateTime createdAt;

	public static TestResponseDto of(TestResult testResult) {
		return TestResponseDto.builder()
			.buddy(testResult.getBuddy())
			.trust(testResult.getTrust())
			.love(testResult.getLove())
			.talk(testResult.getTalk())
			.temperature(testResult.getTemperature())
			.imageUrl(testResult.getImageUrl())
			.createdAt(testResult.getCreatedAt())
			.build();
	}

	public static TestResponseDto of(TestRequestDto testRequestDto, int temperature, String imageUrl) {
		return TestResponseDto.builder()
			.buddy(testRequestDto.getBuddy())
			.trust(testRequestDto.getTrust())
			.love(testRequestDto.getLove())
			.talk(testRequestDto.getTalk())
			.temperature(temperature)
			.imageUrl(imageUrl)
			.createdAt(LocalDateTime.now())
			.build();
	}
}
