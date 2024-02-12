package dnd.donworry.domain.constants;

import io.micrometer.common.lang.Nullable;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorCode {

	/* COMMON */
	NOT_AUTHORIZED_CONTENT("401", "접근 권한이 없습니다."),

	/* TEST */
	TEST_NOT_FOUND("404", "테스트 결과가 존재하지 않습니다."),

	/* AUTH */
	NO_JWT_TOKEN("401", "로그인 정보가 존재하지 않습니다. 다시 로그인해 주세요."),
	NOT_AUTHORIZED_TOKEN("403", "접근 권한이 없습니다."),
	INVALID_REFRESH_TOKEN("401", "로그인 정보가 유효하지 않습니다."),
	INVALID_TOKEN_TYPE("401", "로그인 정보 형식이 올바르지 않습니다."),
	INVALID_TOKEN_STRUCTURE("401", "로그인 정보가 올바르지 않습니다."),
	MODIFIED_TOKEN_DETECTED("401", "로그인 정보가 변경되었습니다."),
	EMAIL_VERIFICATION_FAILED("400", "이메일 인증에 실패하였습니다."),
	EXPIRED_REFRESH_TOKEN("403", "토큰 정보가 만료되었습니다. 로그인이 필요합니다."),
	EXPIRED_TOKEN("401", "토큰이 존재하지 않습니다."),
	VERIFICATION_FAILED("401", "인증에 실패했습니다."),

	/* MEMBER */
	MEMBER_NOT_FOUND("404", "등록되지 않은 회원입니다."),
	FILE_NOT_EXIST("404", "파일이 존재하지 않습니다."),
	SEIZED_TOKEN_DETECTED("403", "토큰 정보가 잘못되었습니다."),
	EMAIL_DUPLICATION("400", "이미 가입된 이메일 입니다."),
	USERNAME_DUPLICATION("400", "이미 존재하는 닉네임 입니다."),

	/* VOTE */
	VOTE_ALREADY_DONE("400", "이미 투표에 참가하셨습니다."),
	VOTE_NOT_FOUND("404", "투표 정보가 존재하지 않습니다."),
	IMAGE_UPLOAD_FAIL("500", "이미지 업로드에 실패하였습니다."),
	SELECTION_SAVE_FAILED("500", "선택지 생성에 실패하였습니다."),

	/* LIKES */
	LIKES_NOT_FOUND("404", "좋아요 정보가 존재하지 않습니다."),
	LIKES_ALREADY_EXISTS("404", "이미 좋아요 정보가 있습니다."),

	/* VALIDATION */
	INVALID_REQUEST("400", "유효하지 않은 입력값 입니다."),

	/* UNEXPECTED */
	UNEXPECTED_EXCEPTION("500", "예상치 못한 에러가 발생하였습니다."),
	SELECTION_SIZE_UNDER_TWO("400", "선택지는 최소 2개 이상이어야 합니다.");

	private final String code;
	private final String message;

	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public <T> ResResult<Object> toResponse(@Nullable T data) {
		return ResResult.builder()
			.code(this.getCode())
			.message(this.getMessage())
			.data(data)
			.build();
	}
}
