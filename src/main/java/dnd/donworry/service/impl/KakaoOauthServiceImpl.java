package dnd.donworry.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnd.donworry.core.factory.YamlPropertySourceFactory;
import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.dto.user.UserDto;
import dnd.donworry.jwt.OAuthToken;
import dnd.donworry.repository.UserRepository;
import dnd.donworry.service.KakaoOauthService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@PropertySource(value = {"classpath:application-kakao.yml"}, factory = YamlPropertySourceFactory.class)
@RequiredArgsConstructor
@Service
public class KakaoOauthServiceImpl implements KakaoOauthService {

    private final UserRepository userRepository;

    @Value("${kakao.clientId}")
    private String clientId;

    @Value("${kakao.redirectUri}")
    private String redirectUri;

    public UserDto login(String code) {
        String kakaoAccessToken = getKakaoAccessToken(code);
        return getUserProfileToken(kakaoAccessToken);
    }

    public String getKakaoAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            ;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return oAuthToken.getAccess_token();
    }

    @Transactional
    public UserDto getUserProfileToken(String accessToken) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                request,
                String.class
        );

        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONObject kakaoAccount = jsonObject.getJSONObject("kakao_account");
        JSONObject profile = kakaoAccount.getJSONObject("profile");

        User user = User.builder()
                .email(kakaoAccount.getString("email"))
                .nickname(profile.getString("nickname"))
                .avatar("1")
                .build();

        return userRepository.findUserByEmail(user.getEmail())
                .map(existingUser -> UserDto.of(existingUser))
                .orElseGet(() -> {
                    userRepository.save(user);
                    return UserDto.of(user);
                });
    }
}
