package dnd.donworry.domain.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "댓글 API ResponseDto")
public class CommentResponseReadDto {
    @Schema(description = "댓글 ID", example = "12")
    private Long commentId;

    @Schema(description = "투표 ID", example = "3")
    private Long voteId;

    @Schema(description = "댓글 생성자", example = "1")
    private Long userId;

    @Schema(description = "댓글 생성자 닉네임", example = "dnd")
    private String nickname;

    @Schema(description = "공감 유무", example = "true")
    private boolean status;

    @Schema(description = "댓글 내용", example = "축의금 내지 말고 밥만 먹고 오세요")
    private String content;

    @Schema(description = "댓글 생성자 아바타", example = "1")
    private String avatar;

    @Schema(description = "댓글 좋아요 개수", example = "21")
    private int likes;

    @Schema(description = "생성일", example = "102403589")
    private LocalDateTime createdAt;

    @Schema(description = "수정일", example = "312553452")
    private LocalDateTime modifiedAt;

}
