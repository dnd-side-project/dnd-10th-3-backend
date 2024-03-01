package dnd.donworry.domain.dto.vote;

import java.util.List;

import dnd.donworry.domain.dto.comment.Pages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotePagingDto {

	private List<VoteResponseDto> list;
	private Pages pages;

	public static VotePagingDto of(List<VoteResponseDto> list, Pages pages) {
		return VotePagingDto.builder()
			.list(list)
			.pages(pages)
			.build();
	}
}
