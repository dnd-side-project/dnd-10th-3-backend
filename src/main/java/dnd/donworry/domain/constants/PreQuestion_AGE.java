package dnd.donworry.domain.constants;

import dnd.donworry.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreQuestion_AGE {
	UNDER_TWENTY("20대 미만"),
	TWENTY("20대"),
	THIRTY("30대"),
	OVER_FORTY("40대 이상");

	private final String age;

	public static PreQuestion_AGE of(String age) {
		for (PreQuestion_AGE preQuestion_AGE : PreQuestion_AGE.values()) {
			if (preQuestion_AGE.age.equals(age)) {
				return preQuestion_AGE;
			}
		}
		throw new CustomException(ErrorCode.AGE_NOT_FOUND);
	}
}
