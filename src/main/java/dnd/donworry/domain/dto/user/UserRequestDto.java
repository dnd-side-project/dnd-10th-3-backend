package dnd.donworry.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequestDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(name = "닉네임 변경 API Request")
    public static class UPDATE {

        @Schema(description = "변경 할 닉네임", example = "dnd")
        private String nickname;
    }
}
