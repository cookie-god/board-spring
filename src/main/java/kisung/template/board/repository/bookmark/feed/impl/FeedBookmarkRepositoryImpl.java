package kisung.template.board.repository.bookmark.feed.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kisung.template.board.repository.bookmark.feed.custom.CustomFeedBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class FeedBookmarkRepositoryImpl implements CustomFeedBookmarkRepository {
  private final JPAQueryFactory jpaQueryFactory;
}
