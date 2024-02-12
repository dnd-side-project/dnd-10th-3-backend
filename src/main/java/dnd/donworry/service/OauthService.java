package dnd.donworry.service;

import dnd.donworry.domain.dto.jwt.TokenResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface OauthService {
    TokenResponseDto loginWithKakao(String code, HttpServletResponse response);
}
