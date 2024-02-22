package dnd.donworry.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnd.donworry.domain.constants.ResResult;
import dnd.donworry.domain.constants.ResponseCode;
import dnd.donworry.service.UserVoteService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/userVote")
@Tag(name = "유저 투표 API", description = "유저의 투표를 관리합니다.")
public class UserVoteController {
	private final UserVoteService userVoteService;

	@PostMapping("/voteId/{voteId}/selectionId/{selectionId}")
	public ResResult<?> attend(@PathVariable("voteId") Long voteId, @PathVariable("selectionId") Long selectionId,
		@Parameter(hidden = true) Authentication authentication, HttpServletResponse response) {
		userVoteService.attend(authentication.getName(), voteId, selectionId);
		return ResponseCode.USER_VOTE_ATTEND.toResponse(null, response);
	}
}
