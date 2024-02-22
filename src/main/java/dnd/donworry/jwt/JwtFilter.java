package dnd.donworry.jwt;

import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.dto.jwt.TokenDto;
import dnd.donworry.domain.entity.RefreshToken;
import dnd.donworry.exception.CustomException;
import dnd.donworry.repository.RefreshTokenRepository;
import dnd.donworry.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class JwtFilter extends OncePerRequestFilter {

    private final String ACCESS_TOKEN_HEADER = "Access-Token";
    private final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            handleTokens(
                    resolveTokenFromCookie(request, ACCESS_TOKEN_HEADER),
                    resolveTokenFromCookie(request, REFRESH_TOKEN_HEADER),
                    request, response);

            filterChain.doFilter(request, response);
    }

    private void handleTokens(String accessToken, String refreshToken, HttpServletRequest request,
                              HttpServletResponse response) {
        if (accessToken != null && refreshToken != null) {
            handleBothTokenExists(accessToken, refreshToken, request);
            return;
        }
        if (accessToken == null && refreshToken != null) {
            handleRefreshTokenOnly(refreshToken, request, response);
            return;
        }
        if (accessToken != null) {
            handleAccessTokenOnly(accessToken, request);
            return;
        }
        handleNoTokens();
    }

    private void handleBothTokenExists(String accessToken, String refreshToken,
                                       HttpServletRequest request) {

        String email = jwtProvider.getEmailFromToken(accessToken);
        RefreshToken refreshTokenOld = fetchRefreshTokenByEmail(email);

        refreshTokenOld.validateRefreshTokenRotate(refreshToken);

        jwtProvider.validateToken(accessToken);
        jwtProvider.validateToken(refreshToken);

        setAuthentication(request, email);
    }

    private RefreshToken fetchRefreshTokenByEmail(String email) {
        log.info(email);
        return refreshTokenRepository.findByValue(email)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
    }

    private RefreshToken fetchRefreshTokenById(String refreshToken) {
        return refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
    }

    private void handleRefreshTokenOnly(String refreshToken, HttpServletRequest request,
                                        HttpServletResponse response) {
        jwtProvider.validateToken(refreshToken);

        RefreshToken refreshTokenOld = fetchRefreshTokenById(refreshToken);
        String email = refreshTokenOld.getValue();

        reissue(email, refreshTokenOld, response);
        setAuthentication(request, email);
    }

    private void handleAccessTokenOnly(String accessToken, HttpServletRequest request) {
        jwtProvider.validateToken(accessToken);

        String email = jwtProvider.getEmailFromToken(accessToken);
        setAuthentication(request, email);
    }

    private void handleNoTokens() {
        throw new CustomException(ErrorCode.NOT_AUTHORIZED_TOKEN);
    }

    private void reissue(String email, RefreshToken refreshTokenOld, HttpServletResponse response) {
        TokenDto tokenDto = jwtProvider.generateJwtToken(email);
        setAccessTokenCookie(response, tokenDto.getAccessToken());
        setRefreshTokenCookie(response, tokenDto.getRefreshToken());
        saveRefreshTokenCache(email, refreshTokenOld, tokenDto);
    }

    private void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        cookieUtil.setCookie(response, ACCESS_TOKEN_HEADER, accessToken,
                jwtProvider.getAccessTokenExpiredTime());
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        cookieUtil.setCookie(response, REFRESH_TOKEN_HEADER, refreshToken,
                jwtProvider.getRefreshTokenExpiredTime());
    }

    private void saveRefreshTokenCache(String email, RefreshToken refreshTokenOld,
                                       TokenDto tokenDto) {
        refreshTokenRepository.deleteById(refreshTokenOld.getKey());

        refreshTokenRepository.save(
                new RefreshToken(tokenDto.getRefreshToken(), email,
                        jwtProvider.getRefreshTokenExpiredTime()));
    }

    private void setAuthentication(HttpServletRequest request, String email) {
        UserAuthentication authentication = new UserAuthentication(email, null, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveTokenFromCookie(HttpServletRequest request, String tokenType) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (tokenType.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
