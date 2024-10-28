package kisung.template.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/apis/v1/feeds")
@RequiredArgsConstructor
@Slf4j
public class FeedController {
  @Operation(summary = "피드 조회", description = "피드 조회 서비스 입니다.")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping(value = "")
  public String getFeeds() {
    return "admin";
  }
}
