package dnd.donworry.domain.dto.user;

import dnd.donworry.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserResponseDto {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(name = "닉네임 변경 API Response")
    public static class UPDATE {

        @Schema(description = "유저 ID", example = "1")
        private Long userId;

        @Schema(description = "유저 닉네임", example = "lazy")
        private String nickname;

        @Schema(description = "변경 시간", example = "2024-02-12T03:12:54")
        private LocalDateTime modifiedAt;

        public static UserResponseDto.UPDATE of(User user) {
            return UPDATE.builder()
                    .userId(user.getId())
                    .nickname(user.getNickname())
                    .modifiedAt(user.getModifiedAt())
                    .build();
        }
    }
}
