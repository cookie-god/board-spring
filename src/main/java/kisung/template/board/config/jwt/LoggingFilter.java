package kisung.template.board.config.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class LoggingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    System.out.println("httpServletRequest.getRequestURI() = " + httpServletRequest.getRequestURI());
    log.info("check filter");
    chain.doFilter(request, response);
    log.info("end filter");
  }
}
