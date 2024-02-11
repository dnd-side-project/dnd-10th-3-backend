package dnd.donworry.exception;

import dnd.donworry.domain.constants.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "CustomException{" +
			"errorCode=" + errorCode +
			'}';
	}

}
