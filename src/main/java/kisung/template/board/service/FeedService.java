package kisung.template.board.service;

import kisung.template.board.dto.FeedDto;
import kisung.template.board.entity.UserInfo;

public interface FeedService {
  FeedDto.PostFeedsRes createFeed(FeedDto.PostFeedsReq postFeedsReq, UserInfo userInfo);
  FeedDto.GetFeedsRes retrieveFeeds(FeedDto.GetFeedsReq getFeedsReq);
}
