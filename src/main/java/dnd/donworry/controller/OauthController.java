package dnd.donworry.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dnd.donworry.domain.constants.ResResult;
import dnd.donworry.domain.constants.ResponseCode;
import dnd.donworry.domain.dto.user.LoginResponseDto;
import dnd.donworry.domain.dto.user.UserResponseDto;
import dnd.donworry.service.OauthService;
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

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Oauth API", description = "소셜 로그인을 통해 유저 로그인, 회원가입 합니다.")
public class OauthController {

	private final OauthService oauthService;

	@GetMapping("/login/oauth2/code/kakao")
	@Operation(summary = "로그인 및 회원가입", description = "카카오 로그인 API로 로그인 및 회원가입 합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 및 회원가입 성공"),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
	})
	public ResResult<LoginResponseDto> login(@Valid @RequestParam(value = "code") String code,
											 HttpServletResponse response) {
		return ResponseCode.MEMBER_LOGIN.toResponse(oauthService.loginWithKakao(code, response));
	}

	@PostMapping("/swaggerUser")
	@Operation(summary = "스웨거 유저 생성", description = "스웨거를 위한 유저를 생성하고 로그인합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "스웨거 유저 생성 성공"),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
			mediaType = "application/json",
			examples = @ExampleObject(value = "{\n  \"code\": \"500\", \n \"message\": \"서버에 에러가 발생했습니다.\"\n}")))
	})
	public ResResult<UserResponseDto.UPDATE> createSwaggerUser(HttpServletResponse response) {
		return ResponseCode.MEMBER_SAVE.toResponse(oauthService.createSwaggerUser(response));
	}
}
