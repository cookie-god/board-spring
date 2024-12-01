package kisung.template.board.repository.bookmark.feed;


import kisung.template.board.entity.FeedBookmark;
import kisung.template.board.repository.bookmark.feed.custom.CustomFeedBookmarkRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBookmarkRepository extends JpaRepository<FeedBookmark, Long>, CustomFeedBookmarkRepository {
}
