package dnd.donworry.exception;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.constants.ResResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	protected ResResult<?> handlerCustomException(CustomException e, HttpServletResponse response) {
		log.error("CustomException: " + e.getErrorCode().getMessage());
		return e.getErrorCode().toResponse(null, response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResResult<?> handleMethodArgumentNotValid(
		MethodArgumentNotValidException e, HttpHeaders h, HttpStatus s, WebRequest w, HttpServletResponse response
	) {
		Map<String, String> validExceptions = e.getBindingResult().getFieldErrors().stream()
			.collect(Collectors.toMap(
				FieldError::getField,
				DefaultMessageSourceResolvable::getDefaultMessage
			));
		log.error("Validation Exception: " + validExceptions);
		return ErrorCode.INVALID_REQUEST.toResponse(validExceptions, response);
	}

	@ExceptionHandler(Exception.class)
	protected ResResult<?> handlerException(Exception e, HttpServletResponse response) {
		log.error("Unexpected_Exception : " + e.getMessage());
		log.error("Unexpected_Exception : " + Arrays.toString(e.getStackTrace()));
		return ErrorCode.UNEXPECTED_EXCEPTION.toResponse(null, response);
	}

}