package dnd.donworry.domain.constants;

import org.springframework.http.HttpStatus;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ResponseCode {
	/*TEST*/
	TEST_SAVED(HttpStatus.OK, "200", "테스트 결과 저장에 성공했습니다."),
	TEST_SUCCESS(HttpStatus.OK, "200", "테스트 결과 생성에 성공했습니다."),
	TEST_FIND_SUCCESS(HttpStatus.OK, "200", "테스트 결과 조회에 성공했습니다."),

	/*AUTH*/
	MEMBER_SAVE(HttpStatus.OK, "200", "회원가입에 성공했습니다."),
	MEMBER_LOGIN(HttpStatus.OK, "200", "로그인에 성공했습니다"),
	MEMBER_LOGOUT(HttpStatus.OK, "200", "로그아웃에 성공했습니다."),
	VERIFICATION_SUCCESS(HttpStatus.OK, "200", "인증에 성공했습니다."),

	/* MEMBER */
	MEMBER_DETAIL(HttpStatus.OK, "200", "회원정보 불러오기에 성공했습니다."),
	MEMBER_UPDATE(HttpStatus.OK, "200", "회원정보 수정에 성공했습니다."),
	MEMBER_DELETE(HttpStatus.OK, "200", "회원정보 삭제에 성공했습니다."),
	MEMBER_EXISTS(HttpStatus.OK, "200", "회원존재 여부 조회에 성공했습니다."),
	AVATAR_UPLOAD(HttpStatus.OK, "200", "이미지 업로드에 성공했습니다."),
	AVATAR_DELETE(HttpStatus.OK, "200", "이미지 삭제에 성공했습니다."),

	/* VOTE */
	VOTING_SUCCESS(HttpStatus.OK, "200", "투표 생성에 성공했습니다."),
	VOTING_UPDATE(HttpStatus.OK, "200", "투표 수정에 성공했습니다."),
	VOTING_DELETE(HttpStatus.OK, "200", "투표 취소에 성공했습니다."),

	/* LIKES */
	LIKES_ADD(HttpStatus.OK, "200", "좋아요 추가에 성공했습니다."),
	LIKES_CANCEL(HttpStatus.OK, "200", "좋아요 취소에 성공했습니다."),

	/* SEARCH */
	SEARCH_SUCCESS(HttpStatus.OK, "200", "검색에 성공했습니다.");

	private final HttpStatus httpStatus;

	private final String code;

	private final String message;

	ResponseCode(HttpStatus httpStatus, String code, String message) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}

	public <T> ResResult<T> toResponse(@Nullable T data) {
		return ResResult.<T>builder()
			.code(this.code)
			.message(this.message)
			.data(data)
			.build();
	}
}