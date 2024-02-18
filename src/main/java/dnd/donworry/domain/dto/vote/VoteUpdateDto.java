package dnd.donworry.domain.dto.vote;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Slf4j
public class VoteUpdateDto {
	@Schema(description = "투표 ID", example = "1")
	private Long id;

	@Schema(description = "투표 제목", example = "축의금은 얼마가 적당할까요?")
	private String title;

	@Schema(description = "투표 내용", example = "절친 결혼식에 축의금을 얼마나 주는게 적당할까요?")
	private String content;

	@Schema(description = "카테고리", example = "축의금")
	private String category;
}
