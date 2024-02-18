package dnd.donworry.domain.dto.selection;

import com.fasterxml.jackson.annotation.JsonInclude;

import dnd.donworry.domain.entity.Selection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "투표 API Response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectionResponseDto {

	@Schema(description = "선택지 ID", example = "1")
	private Long id;

	@Schema(description = "선택지 내용", example = "100만원")
	private String content;

	@Schema(description = "선택지 투표 수", example = "0")
	private int count;

	@Schema(description = "선택지 이미지 경로", example = "https://donworry.s3.ap-northeast-2.amazonaws.com/selection/1")
	private String imagePath;

	@Schema(description = "선택지 투표 비율", example = "0")
	private int votePercentage;

	public static SelectionResponseDto of(Selection selection) {
		return SelectionResponseDto.builder()
			.id(selection.getId())
			.content(selection.getContent())
			.count(selection.getCount())
			.imagePath(selection.getOptionImage().getPath())
			.build();
	}

	public void setVotePercentage(int voteCount, int totalVoteCount) {
		this.votePercentage = (int)((double)voteCount / totalVoteCount * 100);
	}
}
