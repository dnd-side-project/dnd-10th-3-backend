package dnd.donworry.domain.constants;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ResponseCode {

	/*TEST*/
	TEST_SAVED("200", "테스트 결과 저장에 성공했습니다."),
	TEST_SUCCESS("200", "테스트 결과 생성에 성공했습니다."),
	TEST_FIND_SUCCESS("200", "테스트 결과 조회에 성공했습니다."),

	/*AUTH*/
	MEMBER_SAVE("200", "회원가입에 성공했습니다."),
	MEMBER_LOGIN("200", "로그인에 성공했습니다"),
	MEMBER_LOGOUT("200", "로그아웃에 성공했습니다."),
	VERIFICATION_SUCCESS("200", "인증에 성공했습니다."),

	/* MEMBER */
	MEMBER_DETAIL("200", "회원정보 불러오기에 성공했습니다."),
	MEMBER_UPDATE("200", "회원정보 수정에 성공했습니다."),
	MEMBER_DELETE("200", "회원정보 삭제에 성공했습니다."),
	MEMBER_EXISTS("200", "회원존재 여부 조회에 성공했습니다."),
	AVATAR_UPLOAD("200", "이미지 업로드에 성공했습니다."),
	AVATAR_DELETE("200", "이미지 삭제에 성공했습니다."),
	NICKNAME_UPDATE("200", "닉네임 변경에 성공했습니다."),

	/* VOTE */
	VOTE_CREATED("200", "투표 생성에 성공했습니다."),
	VOTE_MODIFIED("200", "투표 수정에 성공했습니다."),
	VOTE_DELETED("200", "투표 취소에 성공했습니다."),

	/* COMMENT */
	COMMENT_CREATE("200", "댓글 생성에 성공했습니다."),
	COMMENT_UPDATE("200", "댓글 수정에 서공했습니다."),
	COMMENT_DELETE("200", "댓글 삭제에 성공했습니다."),
	COMMENT_READ("200", "댓글 조회에 성공했습니다."),

	/* LIKES */
	LIKES_ADD("200", "좋아요 추가에 성공했습니다."),
	LIKES_CANCEL("200", "좋아요 취소에 성공했습니다."),

	/* SEARCH */
	SEARCH_SUCCESS("200", "검색에 성공했습니다."),
	VOTE_FOUND("200", "투표 조회에 성공했습니다."),
	USER_VOTE_ATTEND("200", "유저 투표에 성공했습니다."),
	;

	private final String code;

	private final String message;

	ResponseCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public <T> ResResult<T> toResponse(@Nullable T data, HttpServletResponse response) {
		response.setStatus(Integer.parseInt(this.code));
		return ResResult.<T>builder()
			.code(this.code)
			.message(this.message)
			.data(data)
			.build();
	}
}
