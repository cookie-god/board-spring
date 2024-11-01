package kisung.template.board.repository.feed.impl;

import com.querydsl.core.types.Projections;
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
  public List<FeedDto.FeedRawInfo> findFeedInfos() {
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
      .leftJoin(userInfo).on(feed.userInfo.eq(userInfo)).fetchJoin()
      .orderBy(
        feed.createdAt.desc()
      )
      .fetch();
  }
}
