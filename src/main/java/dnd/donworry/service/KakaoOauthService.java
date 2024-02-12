package dnd.donworry.service;

import dnd.donworry.domain.dto.user.UserDto;

public interface KakaoOauthService {
    UserDto login(String code);

    String getKakaoAccessToken(String code);

    UserDto getUserProfileToken(String accessToken);
}
