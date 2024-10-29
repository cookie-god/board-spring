package kisung.template.board.repository.feed.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kisung.template.board.repository.feed.custom.CustomFeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements CustomFeedRepository {
  private final JPAQueryFactory jpaQueryFactory;

}
