package kisung.template.board.repository.feed.custom;

import kisung.template.board.dto.FeedDto;

import java.util.List;

public interface CustomFeedRepository {
  Long countFeedInfos(FeedDto.GetFeedsReq getFeedsReq);
  List<FeedDto.FeedRawInfo> findFeedInfos(FeedDto.GetFeedsReq getFeedsReq);
}
