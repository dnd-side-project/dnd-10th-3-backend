package dnd.donworry.util;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieUtil {

    /*
    public void setCookie(HttpServletResponse response, String key, String value) {
        ResponseCookie responseCookie = ResponseCookie.from(key, value)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None")
                .build();
        response.addHeader("Set-Cookie", responseCookie.toString());
    }*/

	public void setCookie(HttpServletResponse response, String key, String value, Long expiredTime) {
		ResponseCookie responseCookie = ResponseCookie.from(key, value)
			.httpOnly(true)
			.path("/")
			.secure(true)
			.sameSite("None")
			.maxAge(expiredTime / 1000) //현재 expiredTime은 단위가 ms이고 maxAge는 s이다.
			.build();
		response.addHeader("Set-Cookie", responseCookie.toString());
	}

	public void deleteCookie(HttpServletResponse response, String... keys) {

		for (String key : keys) {
			ResponseCookie responseCookie = ResponseCookie.from(key, "value")
				.httpOnly(true)
				.path("/")
				.secure(true)
				.sameSite("None")
				.domain("localhost")
				.maxAge(0)
				.build();
			response.addHeader("Set-Cookie", responseCookie.toString());
		}

	}

}
