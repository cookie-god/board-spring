package kisung.template.board.service.bookmark;

import kisung.template.board.repository.bookmark.feed.FeedBookmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkServiceImpl implements BookmarkService {
  private final FeedBookmarkRepository feedBookmarkRepository;
}
