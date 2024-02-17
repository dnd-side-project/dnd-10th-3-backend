package dnd.donworry.domain.dto.vote;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import dnd.donworry.domain.constants.Category;
import dnd.donworry.domain.dto.selection.SelectionRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Schema(name = "투표 API Request")
public class VoteRequestDto {
	@Schema(description = "투표 제목", example = "축의금은 얼마가 적당할까요?")
	private String title;

	@Schema(description = "투표 내용", example = "절친 결혼식에 축의금을 얼마나 주는게 적당할까요?")
	private String content;

	@Schema(description = "투표 선택지")
	private List<SelectionRequestDto> selections;

	@Schema(description = "투표 시작일", example = "2021-08-01T00:00:00")
	private LocalDateTime closeDate;

	@Schema(description = "카테고리", example = "축의금")
	private Category category;

	public void mapImages(List<MultipartFile> images) {
		for (int i = 0; i < selections.size(); i++) {
			selections.get(i).setImage(images.get(i));
		}
	}
}
