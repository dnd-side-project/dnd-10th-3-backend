package dnd.donworry.domain.dto.test;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Schema(name = "테스트 결과 API Request")
public class TestRequestDto {
	@Schema(description = "상대방 닉네임", example = "JooHyun")
	private String buddy;

	@Schema(description = "믿음 개수", example = "1")
	private Long trust;

	@Schema(description = "사랑 개수", example = "2")
	private Long love;

	@Schema(description = "대화 개수", example = "3")
	private Long talk;

	public Long[] factorList() {
		return new Long[] {trust, love, talk};
	}
}
