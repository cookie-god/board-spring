package kisung.template.board.repository.feed.custom;

import kisung.template.board.dto.FeedDto;

import java.util.List;

public interface CustomFeedRepository {
  List<FeedDto.FeedRawInfo> findFeedInfos();
}
