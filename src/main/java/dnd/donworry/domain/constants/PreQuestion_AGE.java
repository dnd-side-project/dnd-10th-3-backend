package dnd.donworry.domain.constants;

import dnd.donworry.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreQuestion_AGE {
	TEN("10대"),
	TWENTY("20대"),
	THIRTY("30대"),
	FORTY("40대");

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
