package dnd.donworry.domain.dto.userVote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Slf4j
public class UserVoteRequestDto {
	private Long userId;
	private Long selectionId;
}
