package kisung.template.board.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kisung.template.board.common.code.ErrorCode;
import kisung.template.board.common.response.ErrorResponse;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String DEFAULT_ROLE_PREFIX = "ROLE_";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = request.getHeader(HEADER_AUTHORIZATION);
            if (token != null && token.startsWith(TOKEN_PREFIX)) {
                token = token.substring(TOKEN_PREFIX.length()); // bearer 토큰 만큼 문자열 자름
                jwtTokenProvider.validateToken(token);
                UserInfo userInfo = authService.retrieveUserInfoById(jwtTokenProvider.getUserId(token));
                if (userInfo == null) {
                    throw new BoardException(ErrorCode.NON_EXIST_USER);
                }
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(DEFAULT_ROLE_PREFIX + userInfo.getRole()));
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userInfo, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("Current Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
                filterChain.doFilter(request, response);
            } else {
                throw new AccessDeniedException("invalid token");
            }
        } catch (BoardException e) {
            setErrorResponse(response, ErrorCode.NON_EXIST_USER);
        } catch (AccessDeniedException | NullPointerException e) {
            setErrorResponse(response, ErrorCode.INVALID_TOKEN);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json"); // Content-Type 설정
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
