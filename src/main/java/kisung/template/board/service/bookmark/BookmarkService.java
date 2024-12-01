package kisung.template.board.service.bookmark;

import kisung.template.board.entity.Feed;
import kisung.template.board.entity.UserInfo;

public interface BookmarkService {
  boolean changeFeedBookmark(UserInfo userInfo, Feed feed);
}
