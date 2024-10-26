package kisung.template.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import kisung.template.board.common.response.BasicResponse;
import kisung.template.board.config.SecurityConfig;
import kisung.template.board.config.jwt.JwtTokenProvider;
import kisung.template.board.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kisung.template.board.common.code.SuccessCode.READ_SUCCESS;

@RestController
@RequestMapping(value = "/apis/v1/feeds")
@RequiredArgsConstructor
@Slf4j
public class FeedController {
  @Operation(summary = "피드 조회", description = "피드 조회 서비스 입니다.")
  @PreAuthorize("hasRole('USER')")
  @PostMapping(value = "")
  public String getFeeds() {
    return "admin";
  }
}
