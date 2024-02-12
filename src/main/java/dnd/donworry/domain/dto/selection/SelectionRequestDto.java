package dnd.donworry.domain.dto.selection;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Schema(name = "선택지 API Request")
public class SelectionRequestDto {
	@Schema(description = "선택지 내용", example = "100만원")
	private String content;

	@Schema(description = "선택지 이미지", hidden = true)
	private MultipartFile image;
}
