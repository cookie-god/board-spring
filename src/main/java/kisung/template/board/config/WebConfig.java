package kisung.template.board.config;

import kisung.template.board.config.interceptor.CustomInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
  private final CustomInterceptor customInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(customInterceptor)
        .addPathPatterns("/**");
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // 모든 URL에 대해 CORS 설정
        .allowedOrigins("*") // 모든 도메인 허용
        .allowedMethods("*") // 모든 HTTP 메서드 허용 (GET, POST, PUT, DELETE 등)
        .allowedHeaders("*") // 모든 헤더 허용
        .allowCredentials(false) // 쿠키 허용 설정 (false로 설정)
        .maxAge(3600); // preflight 요청 캐싱 시간 (초 단위)
  }
}
