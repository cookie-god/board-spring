package kisung.template.board.service.bookmark;

import kisung.template.board.entity.Feed;
import kisung.template.board.entity.FeedBookmark;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.repository.bookmark.feed.FeedBookmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static kisung.template.board.enums.Status.ACTIVE;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkServiceImpl implements BookmarkService {
  private final FeedBookmarkRepository feedBookmarkRepository;

  /**
   * 피드 북마크 여부 변경 메서드
   * 1. 피드 북마크 존재 여부 체크
   * 2-1. 피드 북마크 존재 한다면 삭제후 false 리턴
   * 2-2. 피드 북마크 존재 하지 않는 다면 생성후 true 리턴
   */
  @Override
  @Transactional
  public boolean changeFeedBookmark(UserInfo userInfo, Feed feed) {
    FeedBookmark feedBookmark = feedBookmarkRepository.findFeedBookmarkByUserIdAndFeedId(userInfo.getId(), feed.getId()).orElse(null);
    if (feedBookmark != null) {
      feedBookmarkRepository.delete(feedBookmark);
      return false;
    } else {
      feedBookmark = FeedBookmark.of(userInfo, feed);
      feedBookmarkRepository.save(feedBookmark);
      return true;
    }
  }
}
