package dnd.donworry.service;

import org.springframework.transaction.annotation.Transactional;

import dnd.donworry.domain.dto.user.LoginResponseDto;
import dnd.donworry.domain.dto.user.UserResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface OauthService {
	LoginResponseDto loginWithKakao(String code, HttpServletResponse response);

	@Transactional
	UserResponseDto.UPDATE createSwaggerUser(HttpServletResponse response);
}
