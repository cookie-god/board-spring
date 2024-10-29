package kisung.template.board.repository.feed;

import kisung.template.board.entity.Feed;
import kisung.template.board.repository.feed.custom.CustomFeedRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long>, CustomFeedRepository {
}
