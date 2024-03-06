package dnd.donworry.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import dnd.donworry.domain.constants.ResResult;
import dnd.donworry.domain.constants.ResponseCode;
import dnd.donworry.domain.dto.user.UserRequestDto;
import dnd.donworry.domain.dto.user.UserResponseDto;
import dnd.donworry.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "유저 API", description = "회원 탈퇴, 닉네임 변경, 로그아웃합니다.")
public class UserController {

	private final UserService userService;

	@DeleteMapping
	@Operation(summary = "회원 탈퇴", description = "인증된 회원이 서비스를 탈퇴합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
		@ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"403\", \n \"message\": \"접근 권한이 없습니다.\"\n}"))),
		@ApiResponse(responseCode = "401", description = "토큰이 존재하지 않음", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"401\", \n \"message\": \"유효한 토큰이 존재하지 않습니다.\"\n}"))),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
	})
	public ResResult<?> deleteUser(Authentication authentication, HttpServletResponse response) {
		userService.deleteUser(authentication.getName());
		return ResponseCode.MEMBER_DELETE.toResponse(null, response);
	}

	@PatchMapping
	@Operation(summary = "닉네임 변경", description = "인증된 회원이 닉네임을 변경합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "닉네임 변경 성공"),
		@ApiResponse(responseCode = "400", description = "닉네임 변경 실패", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"400\", \n \"message\": \"중복된 닉네임입니다.\"\n}"))),
		@ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"403\", \n \"message\": \"접근 권한이 없습니다.\"\n}"))),
		@ApiResponse(responseCode = "401", description = "토큰이 존재하지 않음", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"401\", \n \"message\": \"유효한 토큰이 존재하지 않습니다.\"\n}"))),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
	})
	public ResResult<UserResponseDto.UPDATE> updateUserNickname(Authentication authentication
		, @Valid @RequestBody UserRequestDto.UPDATE update, HttpServletResponse response) {
		return ResponseCode.NICKNAME_UPDATE.toResponse(userService.updateUserNickname(
			authentication.getName(), update.getNickname()), response);
	}

	@PostMapping("/logout")
	@Operation(summary = "로그아웃", description = "인증된 회원이 로그아웃 합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그아웃 성공"),
		@ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"403\", \n \"message\": \"접근 권한이 없습니다.\"\n}"))),
		@ApiResponse(responseCode = "401", description = "토큰이 존재하지 않음", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"401\", \n \"message\": \"유효한 토큰이 존재하지 않습니다.\"\n}"))),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
	})
	public ResResult<?> logoutUser(Authentication authentication, HttpServletResponse response) {
		userService.logout(authentication.getName(), response);
		return ResponseCode.MEMBER_LOGOUT.toResponse(null, response);
	}

	@GetMapping
	@Operation(summary = "유저 정보 조회", description = "인증된 회원의 정보를 조회 합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "정보 조회 성공"),
			@ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content(
					mediaType = "application/json",
					examples = @ExampleObject(value = "{\n  \"code\": \"403\", \n \"message\": \"접근 권한이 없습니다.\"\n}"))),
			@ApiResponse(responseCode = "401", description = "토큰이 존재하지 않음", content = @Content(
					mediaType = "application/json",
					examples = @ExampleObject(value = "{\n  \"code\": \"401\", \n \"message\": \"유효한 토큰이 존재하지 않습니다.\"\n}"))),
			@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
					mediaType = "application/json",
					examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
	})
	public ResResult<UserResponseDto.READ> getUser(Authentication authentication, HttpServletResponse response) {
		return ResponseCode.MEMBER_DETAIL.toResponse(userService.getUser(authentication.getName()),response);
	}
}
