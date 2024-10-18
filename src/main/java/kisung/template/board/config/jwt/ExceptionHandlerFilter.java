package kisung.template.board.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kisung.template.board.common.code.ErrorCode;
import kisung.template.board.common.response.ErrorResponse;
import kisung.template.board.config.exception.BoardException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {

    try {
      filterChain.doFilter(request, response);
    } catch (BoardException e) {
      //토큰의 유효기간 만료
      setErrorResponse(response, ErrorCode.NON_EXIST_USER);
    }
  }

  private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    ErrorResponse errorResponse = ErrorResponse.of(errorCode);
    ObjectMapper objectMapper = new ObjectMapper();
    response.setContentType("application/json"); // Content-Type 설정
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
