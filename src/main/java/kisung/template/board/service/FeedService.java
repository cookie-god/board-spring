package kisung.template.board.service;

import kisung.template.board.repository.feed.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
  private final FeedRepository feedRepository;
}
