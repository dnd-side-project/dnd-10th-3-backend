package dnd.donworry.domain.dto.vote;

import java.util.List;

import dnd.donworry.domain.dto.selection.SelectionResponseDto;
import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.util.TimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "투표 API Response")
public class VoteResponseDto {

	@Schema(description = "투표 ID", example = "1")
	private Long id;

	@Schema(description = "투표 생성자")
	private User user;

	@Schema(description = "투표 제목", example = "축의금은 얼마가 적당할까요?")
	private String title;

	@Schema(description = "투표 내용", example = "절친 결혼식에 축의금을 얼마나 주는게 적당할까요?")
	private String content;

	@Schema(description = "투표 선택지")
	private List<SelectionResponseDto> selections;

	@Schema(description = "좋아요 개수", example = "0")
	private int likes;

	@Schema(description = "조회수", example = "0")
	private int views;

	@Schema(description = "투표자 수", example = "0")
	private int voters;

	@Schema(description = "투표 상태", example = "false")
	private boolean status;

	@Schema(description = "카테고리", example = "축의금")
	private String category;

	@Schema(description = "투표 마감일", example = "2021-08-01T00:00:00")
	private String closeDate;

	@Schema(description = "생성일", example = "")
	private String createdAt;

	@Schema(description = "수정일", example = "")
	private String updatedAt;

	@Schema(description = "선택한 선택지 ID", example = "1")
	private Long selected;

	public static VoteResponseDto of(Vote vote, List<SelectionResponseDto> selections, Long selectedId) {
		return VoteResponseDto.builder()
			.id(vote.getId())
			.user(vote.getUser())
			.title(vote.getTitle())
			.content(vote.getContent())
			.selections(selections)
			.likes(vote.getLikes())
			.views(vote.getViews())
			.voters(vote.getVoters())
			.status(vote.isStatus())
			.selected(selectedId)
			.category(vote.getCategory().getName())
			.closeDate(TimeUtil.toTimeStampString(vote.getCloseDate()))
			.createdAt(TimeUtil.toTimeStampString(vote.getCreatedAt()))
			.updatedAt(TimeUtil.toTimeStampString(vote.getModifiedAt()))
			.build();
	}
}
