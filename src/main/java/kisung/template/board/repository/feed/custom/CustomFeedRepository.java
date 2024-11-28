package kisung.template.board.repository.feed.custom;

import kisung.template.board.dto.FeedDto;
import kisung.template.board.entity.Feed;

import java.util.List;
import java.util.Optional;

public interface CustomFeedRepository {
  Long countFeedInfos(FeedDto.GetFeedsReq getFeedsReq);

  List<FeedDto.FeedRawInfo> findFeedInfos(FeedDto.GetFeedsReq getFeedsReq);

  Optional<FeedDto.FeedRawInfo> findFeedInfoById(Long feedId);

  Optional<Feed> findFeedById(Long feedId);
}
