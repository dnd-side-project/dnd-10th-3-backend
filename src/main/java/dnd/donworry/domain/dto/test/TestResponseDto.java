package dnd.donworry.domain.dto.test;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import dnd.donworry.domain.constants.RANK;
import dnd.donworry.domain.entity.TestResult;
import dnd.donworry.util.TimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "테스트 결과 API Response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestResponseDto {

	@Schema(description = "테스트 ID", example = "1")
	private Long id;

	@Schema(description = "상대방 나이", example = "20대")
	private String age;

	@Schema(description = "상대방 성별", example = "남자")
	private String gender;

	@Schema(description = "상대방 닉네임", example = "JooHyun")
	private String buddy;

	@Schema(description = "믿음 개수", example = "1")
	private Long trust;

	@Schema(description = "사랑 개수", example = "2")
	private Long love;

	@Schema(description = "대화 개수", example = "3")
	private Long talk;

	@Setter
	@Schema(description = "온도", example = "0")
	private int temperature;

	@Setter
	@Schema(description = "이미지 URL", example = "https://s3.ap-northeast-2.amazonaws.com/donworry/1.png")
	private String imageUrl;

	@Setter
	@Schema(description = "설명",
		example = "내 미래 결혼식에서 \n"
			+ "끝내 나타나지 않을 가능성이 높아요.\n"
			+ "갑자기 모바일 청첩장만 받아 당황했나요?\n"
			+ "축하 이모티콘으로 마음만 보내도 충분해요.\n"
			+ "현명한 결정 기다릴게요~ ")
	private String description;

	@Setter
	@Schema(description = "제목", example = "축하 이모티콘 한 큰 술")
	private String title;

	@Schema(description = "생성일", example = "")
	private String createdAt;

	public static TestResponseDto of(TestResult testResult) {
		TestResponseDto testResponseDto = TestResponseDto.builder()
			.id(testResult.getId())
			.age(testResult.getAge().name())
			.gender(testResult.getGender().name())
			.buddy(testResult.getBuddy())
			.trust(testResult.getTrust())
			.love(testResult.getLove())
			.talk(testResult.getTalk())
			.createdAt(TimeUtil.toTimeStampString(testResult.getCreatedAt()))
			.build();

		return RANK.toTestResponseDto(testResponseDto, testResult.getTemperature());
	}

	public static TestResponseDto of(TestRequestDto testRequestDto, int temperature) {
		TestResponseDto testResponseDto = TestResponseDto.builder()
			.age(testRequestDto.getAge())
			.gender(testRequestDto.getGender())
			.buddy(testRequestDto.getBuddy())
			.trust(testRequestDto.getTrust())
			.love(testRequestDto.getLove())
			.talk(testRequestDto.getTalk())
			.createdAt(TimeUtil.toTimeStampString(LocalDateTime.now()))
			.build();

		return RANK.toTestResponseDto(testResponseDto, temperature);
	}

}
