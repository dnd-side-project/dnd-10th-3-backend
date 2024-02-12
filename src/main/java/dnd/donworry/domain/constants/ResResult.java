package dnd.donworry.domain.constants;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResResult<T> {

	@Schema(description = "응답 코드", example = "200")
	private String code;

	@Schema(description = "응답 메시지", example = "{API 요청}에 성공했습니다.")
	private String message;

	@Schema(description = "응답 데이터")
	private T data;

	@Override
	public String toString() {
		return "ResResult{" +
			", code='" + code + '\'' +
			", message='" + message + '\'' +
			", data=" + data +
			'}';
	}
}
