package dnd.donworry.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnd.donworry.core.factory.YamlPropertySourceFactory;
import dnd.donworry.domain.dto.user.UserDto;
import dnd.donworry.domain.entity.User;
import dnd.donworry.jwt.OAuthToken;
import dnd.donworry.repository.UserRepository;
import dnd.donworry.service.KakaoOauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@PropertySource(value = {"classpath:application-kakao.yml"}, factory = YamlPropertySourceFactory.class)
@RequiredArgsConstructor
@Service
public class KakaoOauthServiceImpl implements KakaoOauthService {

    private final UserRepository userRepository;

    @Value("${kakao.clientId}")
    private String clientId;

    @Value("${kakao.redirectUri}")
    private String redirectUri;

    @Value("${kakao.secretKey}")
    private String secretKey;

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
        params.add("client_secret", secretKey);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        log.info("Kakao API 응답: {}", response.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            log.error("JsonMappingException during token parsing: {}", e.getMessage());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException during token parsing: {}", e.getMessage());
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
        log.info("Kakao API 응답: {}", response.getBody());
        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONObject kakaoAccount = jsonObject.getJSONObject("kakao_account");
        String email = kakaoAccount.getString("email");

        return userRepository.findUserByEmail(email)
                .map(UserDto::of)
                .orElseGet(() -> UserDto.of(userRepository.save(
                        User.builder()
                                .email(email)
                                .avatar("1")
                                .nickname(randomNickname())
                                .build()
                )));
    }

    public String randomNickname() {
        Random random = new Random();

        List<String> adjectives = Arrays.asList(
                "예쁜", "멋진", "밝은", "신나는", "행복한",
                "우아한", "새로운", "신비로운", "따뜻한", "설레는",
                "큰", "작은", "빠른", "느린", "높은",
                "낮은", "차가운", "더운", "강한", "약한",
                "빛나는", "어두운", "부드러운", "거친", "고운",
                "푸른", "붉은", "아름다운", "아담한", "강렬한",
                "부드럽고", "달콤한", "시원한", "따스한", "깨끗한",
                "무거운", "가벼운", "청량한", "달콤한", "맑은",
                "진한", "연하고", "고요한", "건강한", "상쾌한",
                "푸릇한", "산뜻한", "바람직한", "활기찬", "즐거운",
                "화려한", "싱그러운", "생기있는", "새콤한", "따뚜아쥬",
                "천진난만한", "순수한", "담백한", "활달한", "젠틀한",
                "미소나는", "부끄러운", "여린", "소심한", "유쾌한",
                "경쾌한", "유연한", "우수한", "현명한", "천진난만한",
                "건장한", "빛나는", "용감한", "영리한", "강인한",
                "디자인된", "모던한", "아름다운", "아기자기한", "화려한",
                "감각적인", "사랑스러운", "매력적인", "자연스런", "트렌디한",
                "이국적인", "독특한", "아찔한", "훌륭한", "유려한",
                "효율적인", "기발한", "색다른", "환상적인", "유용한",
                "독립적인", "자유로운", "높은", "넓은", "짧은"
        );

        List<String> adverbs = Arrays.asList(
                "잘", "빨리", "아주", "매우", "너무",
                "대단히", "정말", "고요히", "똑똑하게", "차분히",
                "느리게", "신속히", "부드럽게", "단호하게", "확실히",
                "강렬하게", "부드럽게", "간결히", "당당하게", "자세히",
                "명료하게", "정확히", "경쾌하게", "날카롭게", "부담스럽게",
                "꾸밈없이", "경쾌하게", "개인적으로", "공공연히", "주변에서",
                "시원하게", "가끔씩", "가벼운마음으로", "신나게", "정말로",
                "완전히", "완벽하게", "확실하게", "완전히", "놀랍게도",
                "고요하게", "편안하게", "솔직히", "자연스럽게", "자세히",
                "당당하게", "조용하게", "솔직하게", "자세히", "노래처럼",
                "눈부시게", "뜨거운마음으로", "정말로", "과감하게", "행복하게",
                "간결하게", "힘차게", "푹신하게", "깔끔하게", "자세히",
                "활기차게", "신나게", "잘", "빠르게", "어느새",
                "자유롭게", "서둘러", "강렬하게", "간지럽게", "감동적으로",
                "유쾌하게", "뿌듯하게", "즐겁게", "신선하게", "생생하게",
                "정확하게", "한껏", "조용히", "무심코", "흥겹게",
                "지극히", "생각보다", "이처럼", "가끔", "고요히",
                "크게", "가슴속으로", "힘차게", "당당하게", "맑게",
                "솔직하게", "어렵지않게", "가끔은", "완벽하게", "이리저리",
                "그림같게", "간단하게", "다채롭게", "아낌없이", "적극적으로"
        );

        List<String> nouns = Arrays.asList(
                "사랑", "꿈", "희망", "우정", "미소",
                "햇살", "음악", "예술", "자유", "평화",
                "미래", "모험", "자연", "바다", "산",
                "하늘", "별", "강", "꽃", "나무",
                "동물", "책", "지식", "여행", "음식",
                "건강", "힘", "웃음", "성공", "도전",
                "열정", "노력", "희열", "즐거움", "행복",
                "용기", "인내", "성실", "믿음", "열정",
                "긍정", "자부심", "자존감", "자유", "존경",
                "이해", "소통", "교감", "영감", "창조",
                "발전", "존경", "역경", "휴식", "변화",
                "소망", "가치", "문화", "존경", "눈물",
                "기적", "흥미", "사실", "선물", "경험",
                "흐름", "의미", "흥미", "추억", "현재",
                "불안", "역사", "사상", "정의", "진리",
                "실천", "예언", "기쁨", "활기", "열기",
                "감동", "생명", "전망", "세계", "존경",
                "청춘", "존재", "현실", "인생", "가치",
                "군림", "원동력", "변화", "자부심", "표현",
                "상징", "영혼", "희망", "사회", "나아감"
        );
        String adjective = adjectives.get(random.nextInt(adjectives.size()));
        String adverb = adverbs.get(random.nextInt(adverbs.size()));
        String noun = nouns.get(random.nextInt(nouns.size()));
        String number = String.valueOf(random.nextInt(10001));
        return adverb + adjective + noun + "@" + number;
    }
}
