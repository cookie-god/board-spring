package kisung.template.board.service.feed;

import kisung.template.board.dto.FeedDto;
import kisung.template.board.entity.Feed;
import kisung.template.board.entity.UserInfo;

public interface FeedService {
  FeedDto.PostFeedsRes createFeeds(FeedDto.PostFeedsReq postFeedsReq, UserInfo userInfo);

  FeedDto.GetFeedsRes retrieveFeeds(FeedDto.GetFeedsReq getFeedsReq);

  FeedDto.PutFeedsRes editFeeds(FeedDto.PutFeedsReq putFeedsReq, UserInfo userInfo);

  FeedDto.DeleteFeedsRes deleteFeeds(FeedDto.DeleteFeedsReq deleteFeedsReq, UserInfo userInfo);

  FeedDto.GetFeedRes retrieveFeed(Long feedId, UserInfo userInfo);

  Feed retrieveFeedEntity(Long feedId);
}
