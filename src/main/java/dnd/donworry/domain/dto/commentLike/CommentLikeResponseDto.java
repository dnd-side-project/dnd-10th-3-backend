package dnd.donworry.domain.dto.commentLike;

import com.fasterxml.jackson.annotation.JsonInclude;

import dnd.donworry.domain.entity.Comment;
import dnd.donworry.domain.entity.CommentLike;
import dnd.donworry.util.TimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "공감 API ResponseDto")
public class CommentLikeResponseDto {
	@Schema(description = "댓글 ID", example = "1")
	private Long commentId;

	@Schema(description = "댓글 내용", example = "축의금 내지 말고 밥만 먹고 오세요")
	private String content;

	@Schema(description = "생성일", example = "2024-03-03T03:03:03")
	private String createdAt;

	@Schema(description = "수정일", example = "2024-03-04T03:03:03")
	private String modifiedAt;

	@Schema(description = "댓글 좋아요 개수", example = "21")
	private int likes;

	@Schema(description = "댓글 좋아요 상태", example = "false")
	private boolean status;

	public static CommentLikeResponseDto of(Comment comment, CommentLike commentLike) {
		return CommentLikeResponseDto.builder()
			.commentId(comment.getId())
			.content(comment.getContent())
			.likes(comment.getLikes())
			.createdAt(TimeUtil.toTimeStampString(commentLike.getCreatedAt()))
			.modifiedAt(TimeUtil.toTimeStampString(commentLike.getModifiedAt()))
			.status(commentLike.isStatus())
			.build();
	}
}
