package dnd.donworry.domain.constants;

import dnd.donworry.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreQuestion_Gender {
	MALE("남자"),
	FEMALE("여자");

	private final String gender;

	public static PreQuestion_Gender of(String age) {
		for (PreQuestion_Gender preQuestion_gender : PreQuestion_Gender.values()) {
			if (preQuestion_gender.gender.equals(age)) {
				return preQuestion_gender;
			}
		}
		throw new CustomException(ErrorCode.GENDER_NOT_FOUND);
	}
}
