package kisung.template.board.repository.bookmark.feed.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kisung.template.board.entity.FeedBookmark;
import kisung.template.board.repository.bookmark.feed.custom.CustomFeedBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static kisung.template.board.entity.QFeedBookmark.feedBookmark;


@Repository
@RequiredArgsConstructor
public class FeedBookmarkRepositoryImpl implements CustomFeedBookmarkRepository {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Optional<FeedBookmark> findFeedBookmarkByUserIdAndFeedId(Long userId, Long feedId) {
    return Optional.ofNullable(
        jpaQueryFactory
            .select(feedBookmark)
            .from(feedBookmark)
            .where(
                feedBookmark.userInfo.id.eq(userId),
                feedBookmark.feed.id.eq(feedId)
            )
            .fetchFirst()
    );
  }
}
