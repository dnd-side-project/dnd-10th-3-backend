package dnd.donworry.service.impl;

import dnd.donworry.domain.dto.jwt.TokenDto;
import dnd.donworry.domain.dto.jwt.TokenResponseDto;
import dnd.donworry.domain.dto.user.UserDto;
import dnd.donworry.domain.entity.RefreshToken;
import dnd.donworry.jwt.JwtProvider;
import dnd.donworry.repository.RefreshTokenRepository;
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
    private final JwtProvider jwtProvider;
    private final KakaoOauthService kakaoOauthService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieUtil cookieUtil;

    private final String ACCESS_TOKEN = "Access-Token";
    private final String REFRESH_TOKEN = "Refresh-Token";

    @Transactional
    public TokenResponseDto loginWithKakao(String code, HttpServletResponse response) {
        UserDto userDto = kakaoOauthService.login(code);
        TokenDto tokenDto = jwtProvider.generateJwtToken(userDto.getEmail());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByValue(userDto.getEmail());
        refreshToken.ifPresent(refreshTokenRepository::delete);
        refreshTokenRepository.save(new RefreshToken(tokenDto.getRefreshToken(), userDto.getEmail(), jwtProvider.getRefreshTokenExpiredTime()));
        cookieUtil.setCookie(response, REFRESH_TOKEN, tokenDto.getRefreshToken());
        cookieUtil.setCookie(response, ACCESS_TOKEN, tokenDto.getAccessToken());

        return TokenResponseDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
    }
}