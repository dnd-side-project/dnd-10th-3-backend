package dnd.donworry.domain.dto.test;

import java.time.LocalDateTime;

import dnd.donworry.domain.entity.TestResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "테스트 결과 API Response")
public class TestResponseDto {

	@Schema(description = "상대방 닉네임", example = "JooHyun")
	private String buddy;

	@Schema(description = "믿음 개수", example = "1")
	private Long trust;

	@Schema(description = "사랑 개수", example = "2")
	private Long love;

	@Schema(description = "대화 개수", example = "3")
	private Long talk;

	// TODO: 등급에 따른 실제 온도를 반환하도록 수정
	@Schema(description = "온도", example = "4")
	private int temperature;

	@Schema(description = "이미지 URL", example = "https://s3.ap-northeast-2.amazonaws.com/donworry/1.png")
	private String imageUrl;

	@Schema(description = "생성일", example = "2021-07-01T00:00:00")
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
