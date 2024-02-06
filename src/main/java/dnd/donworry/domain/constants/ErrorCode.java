package dnd.donworry.domain.constants;

import io.micrometer.common.lang.Nullable;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public enum ErrorCode {

    /* COMMON */
    NOT_AUTHORIZED_CONTENT(HttpStatus.UNAUTHORIZED, "401", "접근 권한이 없습니다."),

    /* AUTH */
    NO_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "401", "로그인 정보가 존재하지 않습니다. 다시 로그인해 주세요."),
    NOT_AUTHORIZED_TOKEN(HttpStatus.FORBIDDEN, "403", "접근 권한이 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "401", "로그인 정보가 유효하지 않습니다."),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "401", "로그인 정보 형식이 올바르지 않습니다."),
    INVALID_TOKEN_STRUCTURE(HttpStatus.UNAUTHORIZED, "401", "로그인 정보가 올바르지 않습니다."),
    MODIFIED_TOKEN_DETECTED(HttpStatus.UNAUTHORIZED, "401", "로그인 정보가 변경되었습니다."),
    EMAIL_VERIFICATION_FAILED(HttpStatus.BAD_REQUEST, "400", "이메일 인증에 실패하였습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "403", "토큰 정보가 만료되었습니다. 로그인이 필요합니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "401", "토큰이 존재하지 않습니다."),

    /* MEMBER */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "등록되지 않은 회원입니다."),
    FILE_NOT_EXIST(HttpStatus.NOT_FOUND, "404", "파일이 존재하지 않습니다."),
    SEIZED_TOKEN_DETECTED(HttpStatus.FORBIDDEN, "403", "토큰 정보가 잘못되었습니다."),
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "400", "이미 가입된 이메일 입니다."),
    USERNAME_DUPLICATION(HttpStatus.BAD_REQUEST, "400", "이미 존재하는 닉네임 입니다."),

    /* VOTE */
    VOTE_ALREADY_DONE(HttpStatus.BAD_REQUEST, "400", "이미 투표에 참가하셨습니다."),
    VOTE_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "투표 정보가 존재하지 않습니다."),

    /* LIKES */
    LIKES_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "좋아요 정보가 존재하지 않습니다."),
    LIKES_ALREADY_EXISTS(HttpStatus.NOT_FOUND, "404", "이미 좋아요 정보가 있습니다."),

    /* VALIDATION */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "400", "유효하지 않은 입력값 입니다."),

    /* UNEXPECTED */
    UNEXPECTED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "500", "예상치 못한 에러가 발생하였습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public <T> ResponseEntity<Object> toResponse(@Nullable T data) {
        return new ResponseEntity<>(ResResult.builder()
                .errorCode(this)
                .code(this.getCode())
                .message(this.getMessage())
                .data(data)
                .build(), this.status);
    }
}
