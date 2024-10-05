package kisung.template.board.controller;

import kisung.template.board.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/apis/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
  private final UserService userService;
}
