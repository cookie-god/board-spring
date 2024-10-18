package kisung.template.board.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kisung.template.board.common.code.ErrorCode;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthService authService;
  private static final String HEADER_AUTHORIZATION = "Authorization";
  private static final String TOKEN_PREFIX = "Bearer ";


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String token = request.getHeader(HEADER_AUTHORIZATION);
    if (token != null && token.startsWith(TOKEN_PREFIX)) {
      token = token.substring(TOKEN_PREFIX.length()); // bearer 토큰 만큼 문자열 자름
      if (jwtTokenProvider.validateToken(token)) {
        UserInfo userInfo = authService.retrieveUserInfoById(jwtTokenProvider.getUserId(token));
        if (userInfo == null) {
          throw new BoardException(ErrorCode.NON_EXIST_USER);
        }
        log.info("userInfo = {}", userInfo);
        UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userInfo, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
    filterChain.doFilter(request, response);
  }


}