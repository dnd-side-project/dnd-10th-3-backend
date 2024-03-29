package dnd.donworry.service.impl;

import dnd.donworry.domain.dto.jwt.TokenDto;
import dnd.donworry.domain.dto.user.LoginResponseDto;
import dnd.donworry.domain.dto.user.UserDto;
import dnd.donworry.domain.dto.user.UserResponseDto;
import dnd.donworry.domain.entity.RefreshToken;
import dnd.donworry.domain.entity.User;
import dnd.donworry.jwt.JwtProvider;
import dnd.donworry.repository.RefreshTokenRepository;
import dnd.donworry.repository.UserRepository;
import dnd.donworry.service.KakaoOauthService;
import dnd.donworry.service.OauthService;
import dnd.donworry.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthServiceImpl implements OauthService {
	private static final String ACCESS_TOKEN = "Access-Token";
	private static final String REFRESH_TOKEN = "Refresh-Token";
	private final JwtProvider jwtProvider;
	private final KakaoOauthService kakaoOauthService;
	private final RefreshTokenRepository refreshTokenRepository;
	private final CookieUtil cookieUtil;
	private final UserRepository userRepository;

	@Transactional
	public LoginResponseDto loginWithKakao(String code, HttpServletResponse response) {
		UserDto userDto = kakaoOauthService.login(code);
		TokenDto tokenDto = jwtProvider.generateJwtToken(userDto.getEmail());
		createToken(userDto.getEmail(), response);
		log.info("oauthServiceImpl.code = {}", code);
		log.info("loginWithKakao.nickname = {}", userDto.getNickname());
		return LoginResponseDto.builder()
			.accessToken(tokenDto.getAccessToken())
			.refreshToken(tokenDto.getRefreshToken())
			.nickname(userDto.getNickname())
			.build();
	}

	@Transactional
	public void createToken(String email, HttpServletResponse response) {
		TokenDto tokenDto = jwtProvider.generateJwtToken(email);
		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByValue(email);
		refreshToken.ifPresent(refreshTokenRepository::delete);
		refreshTokenRepository.save(
			new RefreshToken(tokenDto.getRefreshToken(), email, jwtProvider.getRefreshTokenExpiredTime()/1000));
		cookieUtil.setCookie(response, REFRESH_TOKEN, tokenDto.getRefreshToken(),
			jwtProvider.getRefreshTokenExpiredTime());
		cookieUtil.setCookie(response, ACCESS_TOKEN, tokenDto.getAccessToken(),
			jwtProvider.getAccessTokenExpiredTime());
	}

	@Transactional
	@Override
	public UserResponseDto.UPDATE createSwaggerUser(HttpServletResponse response) {
		User user = User.builder()
			.email("test@test.com")
			.nickname("test")
			.avatar("")
			.build();
		userRepository.save(user);
		log.info("Swagger User Created");
		createToken(user.getEmail(), response);
		return UserResponseDto.UPDATE.of(user);
	}
}
