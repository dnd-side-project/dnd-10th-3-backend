package dnd.donworry.domain.constants;

import dnd.donworry.domain.dto.test.TestResponseDto;
import dnd.donworry.exception.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@RequiredArgsConstructor
@Slf4j
public enum RANK {

	ZERO(1,
		0,
		"https://s3.ap-northeast-2.amazonaws.com/donworry/0.png",
		"축하 이모티콘 한 큰 술",
		"만나면 어색한 기류가 흐르는 관계로 보여요.\n"
			+ "아쉽지만 내 미래 결혼식에서 \n"
			+ "끝내 나타나지 않을 가능성이 높아요.\n"
			+ "갑자기 모바일 청첩장만 받아 당황했나요?\n"
			+ "축하 이모티콘으로 마음만 보내도 충분해요.\n"
			+ "현명한 결정 기다릴게요~ "),
	ONE(2,
		36,
		"https://s3.ap-northeast-2.amazonaws.com/donworry/1.png",
		"최소한의 성의 표시하는 남남",
		"가깝지도 멀지도 않은 미지근한 관계로 보여요.\n"
			+ "내 미래 결혼식, 멀리서 유튜브에 집중하지만\n"
			+ "성의 표시하는 참석러 남남이네요.\n"
			+ "친하지는 않지만 사회적 친분으로 참석할 지,\n"
			+ "축의금을 낼 지 고민하고 있었나요?\n"
			+ "최소한의 성의 표시만 추천드려요."),
	TWO(3,
		70,
		"https://s3.ap-northeast-2.amazonaws.com/donworry/2.png",
		"단체 플래시샷 1인분 찜",
		"아주 친하지는 않지만 친분이 있는 관계네요.\n"
			+ "주기적으로 만남을 이어오고 있었나요?\n"
			+ "특별한 날, 내 미래 결혼식에서 \n"
			+ "단체사진 한 켠을 채워줄 의리있는 사람이네요.\n"
			+ "특별한 순간에 플래시로 최선을 다해 \n"
			+ "비춰줄 미래 모습이 그려지네요."),
	THREE(4,
		100,
		"https://s3.ap-northeast-2.amazonaws.com/donworry/3.png",
		"내 부모님 뒷자리에 앉을 찐친",
		"당신과 상대는 친밀도가 매우 높은 관계네요.\n"
			+ "내 인생에서 꼭 필요한 존재라고 느꼈나요?\n"
			+ "내 미래 결혼식날, 진심을 다해 축하해줄 사람으로\n"
			+ "부모님 뒷자리에 앉을 찐친이에요.\n"
			+ "돈으로 환산하기 어려울 정도의 관계라\n"
			+ "추천 금액 이상의 것을 표현해도 좋아요."),
	;

	private final int level;
	private final int temperature;
	private final String imageUrl;
	private final String title;
	private final String description;

	public static RANK findByTemperature(int temperature) {
		log.info("temperature: {}", temperature);
		for (RANK rank : values()) {
			if (rank.getLevel() == temperature) {
				return rank;
			}
		}
		throw new CustomException(ErrorCode.RANK_NOT_FOUND);
	}

	public static TestResponseDto toTestResponseDto(TestResponseDto testResponseDto, int temperature) {
		RANK rank = findByTemperature(temperature);
		testResponseDto.setTemperature(rank.getTemperature());
		testResponseDto.setImageUrl(rank.getImageUrl());
		testResponseDto.setTitle(rank.getTitle());
		testResponseDto.setDescription(rank.getDescription());
		return testResponseDto;
	}

	public static RANK of(int temperature) {
		for (RANK rank : values()) {
			if (rank.getTemperature() == temperature) {
				return rank;
			}
		}
		throw new CustomException(ErrorCode.RANK_NOT_FOUND);
	}
}
