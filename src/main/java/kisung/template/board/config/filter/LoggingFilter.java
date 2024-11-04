package kisung.template.board.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class LoggingFilter extends OncePerRequestFilter {
  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(MediaType.valueOf("text/*"), MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.valueOf("application/*+json"), MediaType.valueOf("application/*+xml"), MediaType.MULTIPART_FORM_DATA);

  private final boolean enabled = true;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    if (isAsyncDispatch(request)) {
      filterChain.doFilter(request, response);
    } else {
      doFilterWrapper(wrapRequest(request), wrapResponse(response), filterChain);
    }

  }

  protected void doFilterWrapper(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws IOException, ServletException {
    StringBuilder msg = new StringBuilder();
    try {
      beforeRequest(request, response, msg);
      filterChain.doFilter(request, response);
    } finally {
      response.copyBodyToResponse();
      afterRequest(request, response, msg);
      if (log.isInfoEnabled()) {
        log.info(msg.toString());
      }
    }
  }

  protected void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, StringBuilder msg) {
    if (enabled && log.isInfoEnabled()) {
      msg.append("\n-- REQUEST --\n");
      logRequestHeader(request, request.getRemoteAddr() + "|>", msg);
    }
  }

  protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, StringBuilder msg) {
    if (enabled && log.isInfoEnabled()) {
      logRequestBody(request, request.getRemoteAddr() + "|>", msg);
      msg.append("\n-- RESPONSE --\n");
      logResponse(response, request.getRemoteAddr() + "|<", msg);
    }
  }

  private static void logRequestHeader(ContentCachingRequestWrapper request, String prefix, StringBuilder msg) {
    String queryString = request.getQueryString();
    if (queryString == null) {
      msg.append(String.format("%s %s %s", prefix, request.getMethod(), request.getRequestURI())).append("\n");
    } else {
      msg.append(String.format("%s %s %s?%s", prefix, request.getMethod(), request.getRequestURI(), queryString)).append("\n");
    }
  }

  private static void logRequestBody(ContentCachingRequestWrapper request, String prefix, StringBuilder msg) {
    byte[] content = request.getContentAsByteArray();
    if (content.length > 0) {
      logContent(content, request.getContentType(), request.getCharacterEncoding(), prefix, msg);
    }
  }

  private static void logResponse(ContentCachingResponseWrapper response, String prefix, StringBuilder msg) {
    int status = response.getStatus();
    msg.append(String.format("%s %s %s", prefix, status, HttpStatus.valueOf(status).getReasonPhrase())).append("\n");
    byte[] content = response.getContentAsByteArray();
    if (content.length > 0) {
      logContent(content, response.getContentType(), response.getCharacterEncoding(), prefix, msg);
    }
  }

  private static void logContent(byte[] content, String contentType, String contentEncoding, String prefix, StringBuilder msg) {
    MediaType mediaType = MediaType.valueOf(contentType);
    boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
    if (visible) {
      try {
        String contentString = new String(content, contentEncoding);
        Stream.of(contentString.split("\r\n|\r|\n")).forEach(line -> msg.append(prefix).append(" ").append(line).append("\n"));
      } catch (UnsupportedEncodingException e) {
        msg.append(String.format("%s [%d bytes content]", prefix, content.length)).append("\n");
      }
    } else {
      msg.append(String.format("%s [%d bytes content]", prefix, content.length)).append("\n");
    }
  }

  private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
    if (request instanceof ContentCachingRequestWrapper) {
      return (ContentCachingRequestWrapper) request;
    } else {
      return new ContentCachingRequestWrapper(request);
    }
  }

  private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
    if (response instanceof ContentCachingResponseWrapper) {
      return (ContentCachingResponseWrapper) response;
    } else {
      return new ContentCachingResponseWrapper(response);
    }
  }
}
