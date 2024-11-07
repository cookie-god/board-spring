package kisung.template.board.repository.feed.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kisung.template.board.dto.FeedDto;
import kisung.template.board.repository.feed.custom.CustomFeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kisung.template.board.entity.QFeed.feed;
import static kisung.template.board.entity.QUserInfo.userInfo;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements CustomFeedRepository {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Long countFeedInfos(FeedDto.GetFeedsReq getFeedsReq) {
    return jpaQueryFactory
        .select(feed.count())
        .from(feed)
        .where(search(getFeedsReq.getSearchKeyword()))
        .fetchFirst();
  }

  @Override
  public List<FeedDto.FeedRawInfo> findFeedInfos(FeedDto.GetFeedsReq getFeedsReq) {
    return jpaQueryFactory
        .select(Projections.bean(FeedDto.FeedRawInfo.class,
            feed.id,
            feed.content,
            userInfo.id.as("userId"),
            userInfo.email,
            userInfo.nickname,
            userInfo.role,
            feed.commentCnt,
            feed.bookmarkCnt,
            feed.createdAt,
            feed.updatedAt
        ))
        .from(feed)
        .innerJoin(userInfo).on(feed.userInfo.eq(userInfo)).fetchJoin()
        .where(
            search(getFeedsReq.getSearchKeyword()),
            cursorId(getFeedsReq.getFeedId())
        )
        .orderBy(
            feed.id.desc()
        )
        .limit(getFeedsReq.getSize())
        .fetch();
  }

  private BooleanExpression cursorId(Long feedId) {
    return feedId != 0 ? feed.id.lt(feedId) : null;
  }
  private BooleanExpression search(String searchKeyword) {
    if (searchKeyword != null) {
      return feed.content.startsWith(searchKeyword.toUpperCase());
    }
    return null;
  }
}
