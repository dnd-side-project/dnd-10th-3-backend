package dnd.donworry.exception;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.constants.ResResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ExceptionHandleFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (CustomException e) {
			handleCustomException(e, response);
		}
	}

	private void handleCustomException(CustomException e, HttpServletResponse response) throws IOException {
		log.error("CustomException", e);
		ResResult<?> errorResponse = buildErrorResponse(e);
		setResponse(response, errorResponse);
	}

	private ResResult<?> buildErrorResponse(CustomException e) {
		String errorMessage = e.getErrorCode().getMessage();
		ErrorCode errorCode = e.getErrorCode();
		return ResResult.builder().code(errorCode.getCode()).message(errorMessage).build();
	}

	private void setResponse(HttpServletResponse response, ResResult<?> errorResponse) throws IOException {
		response.setStatus(Integer.parseInt(errorResponse.getCode()));
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(errorResponse.toString());
	}
}