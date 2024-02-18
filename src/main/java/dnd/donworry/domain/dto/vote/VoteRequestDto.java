package dnd.donworry.domain.dto.vote;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.dto.selection.SelectionRequestDto;
import dnd.donworry.exception.CustomException;
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
@Schema(name = "투표 API Request")
@Slf4j
public class VoteRequestDto {

	@Schema(description = "투표 제목", example = "축의금은 얼마가 적당할까요?")
	private String title;

	@Schema(description = "투표 내용", example = "절친 결혼식에 축의금을 얼마나 주는게 적당할까요?")
	private String content;

	@Schema(description = "투표 선택지")
	private List<SelectionRequestDto> selections;

	@Schema(description = "투표 시작일", example = "2021-08-01")
	private LocalDate closeDate;

	@Schema(description = "카테고리", example = "축의금")
	private String category;

	// @TODO: 지현님과 상의 후 반환 결정, 투표 CRUD
	public void mapImages(List<MultipartFile> images) {
		log.info("이미지 매핑 시작");
		if (images.size() != selections.size()) {
			throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAIL);
		}
		for (int i = 0; i < images.size(); i++) {
			selections.get(i).setImage(images.get(i).isEmpty() ? null : images.get(i));
		}
		log.info("이미지 매핑 완료");
	}
}
