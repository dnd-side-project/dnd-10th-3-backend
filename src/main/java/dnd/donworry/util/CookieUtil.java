package dnd.donworry.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    public void setCookie(HttpServletResponse response, String key, String value) {
        ResponseCookie responseCookie = ResponseCookie.from(key, value)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None")
                .build();
        response.addHeader("Set-Cookie", responseCookie.toString());
    }

    public void setCookie(HttpServletResponse response, String key, String value, Long expiredTime) {
        ResponseCookie responseCookie = ResponseCookie.from(key, value)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None")
                .maxAge(expiredTime)
                .build();
        response.addHeader("Set-Cookie", responseCookie.toString());
    }

    public void deleteCookie(HttpServletResponse response, String... keys) {
        for (String key : keys) {
            Cookie cookie = new Cookie(key, null);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

    }

}
