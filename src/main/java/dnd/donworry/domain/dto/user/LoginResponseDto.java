package dnd.donworry.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "토큰 API Response")
public class LoginResponseDto {

    @Schema(name = "accessToken", example = "ACCESS_TOKEN")
    private String accessToken;

    @Schema(name = "refreshToken", example = "REFRESH_TOKEN")
    private String refreshToken;

    @Schema(name = "nickname", example = "행복한10기3조@123")
    private String nickname;
}
