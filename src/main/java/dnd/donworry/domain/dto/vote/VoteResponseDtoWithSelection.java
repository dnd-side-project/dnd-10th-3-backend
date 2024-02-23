package dnd.donworry.domain.dto.vote;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoteResponseDtoWithSelection {

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
	private LocalDate closeDate;

	@Schema(description = "생성일", example = "")
	private String createdAt;

	@Schema(description = "수정일", example = "")
	private String updatedAt;

	@Schema(description = "선택한 선택지 ID", example = "1")
	private Long selected;

	public static VoteResponseDtoWithSelection of(Vote bestVote) {
		VoteResponseDtoWithSelection voteResponseDtoWithSelection = VoteResponseDtoWithSelection.builder()
			.id(bestVote.getId())
			.user(bestVote.getUser())
			.title(bestVote.getTitle())
			.content(bestVote.getContent())
			.likes(bestVote.getLikes())
			.views(bestVote.getViews())
			.voters(bestVote.getVoters())
			.status(bestVote.isStatus())
			.category(bestVote.getCategory().getName())
			.closeDate(bestVote.getCloseDate())
			.createdAt(TimeUtil.toTimeStampString(bestVote.getCreatedAt()))
			.updatedAt(TimeUtil.toTimeStampString(bestVote.getModifiedAt()))
			.build();

		voteResponseDtoWithSelection.selections = bestVote.getSelections()
			.stream()
			.map(SelectionResponseDto::of)
			.toList();

		return voteResponseDtoWithSelection;
	}
}
