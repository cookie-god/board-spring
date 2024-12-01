package kisung.template.board.repository.bookmark.feed.custom;

import kisung.template.board.entity.FeedBookmark;

import java.util.Optional;

public interface CustomFeedBookmarkRepository {
  Optional<FeedBookmark> findFeedBookmarkByUserIdAndFeedId(Long userId, Long feedId);
}
