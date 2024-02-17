package dnd.donworry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import dnd.donworry.exception.ExceptionHandleFilter;
import dnd.donworry.jwt.JwtFilter;
import dnd.donworry.jwt.JwtProvider;
import dnd.donworry.repository.RefreshTokenRepository;
import dnd.donworry.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	private final JwtProvider jwtProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	private final CookieUtil cookieUtil;

	@Bean
	public AuthenticationManager authenticationManager(
		final AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain configure(final HttpSecurity http) throws Exception {

		return http
			.cors(cors -> cors.disable())
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/login/oauth2/code/kakao", "/", "/index.html", "swagger-ui/**", "/v3/api-docs/**")
				.permitAll()
				.anyRequest()
				.authenticated())
			.addFilterBefore(new JwtFilter(jwtProvider, cookieUtil, refreshTokenRepository),
				UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new ExceptionHandleFilter(), UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		// 아래 url은 filter 에서 제외
		return web ->
			web.ignoring()
				.requestMatchers("/v3/api-docs/**")
				.requestMatchers("/login/oauth2/code/kakao", "/", "/index.html")
				.requestMatchers("/swagger-resources/**")
				.requestMatchers("/swagger-ui/**")
				.requestMatchers("/webjars/**")
				.requestMatchers("/swagger/**")
				.requestMatchers("/swaggerUser")
				.requestMatchers("/api-docs/**")
				.requestMatchers("/swagger-ui/**");
	}

}

