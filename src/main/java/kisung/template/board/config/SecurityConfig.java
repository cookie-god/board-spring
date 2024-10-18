package kisung.template.board.config;

import kisung.template.board.config.jwt.ExceptionHandlerFilter;
import kisung.template.board.config.jwt.JwtAuthenticationFilter;
import kisung.template.board.config.jwt.JwtTokenProvider;
import kisung.template.board.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthService authService;


  /**
   * PasswordEncoder를 Bean으로 등록
   */
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * 인증/인가에 대한 설정
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(authz -> authz
        .anyRequest().permitAll()  // 모든 요청을 허용
      )
      .addFilterBefore(new ExceptionHandlerFilter(), UsernamePasswordAuthenticationFilter.class)
      .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, authService), UsernamePasswordAuthenticationFilter.class)
    ;  // CSRF 비활성화


    return http.build();
  }
}
