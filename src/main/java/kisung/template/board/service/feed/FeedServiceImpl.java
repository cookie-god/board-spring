package kisung.template.board.service.feed;

import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.FeedDto;
import kisung.template.board.dto.UserDto;
import kisung.template.board.entity.Feed;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.repository.feed.FeedRepository;
import kisung.template.board.service.bookmark.BookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static kisung.template.board.common.code.ErrorCode.*;
import static kisung.template.board.enums.Status.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
  private final FeedRepository feedRepository;
  private final BookmarkService bookmarkService;

  /**
   * 피드 생성 서비스
   */
  @Override
  @Transactional
  public FeedDto.PostFeedsRes createFeeds(FeedDto.PostFeedsReq postFeedsReq, UserInfo userInfo) {
    validate(postFeedsReq);
    Feed feed = Feed.of(postFeedsReq.getContent(), userInfo);
    feed = feedRepository.save(feed);
    return FeedDto.PostFeedsRes.builder()
        .feedId(feed.getId())
        .build();
  }

  /**
   * 피드 조회 서비스
   */
  @Override
  public FeedDto.GetFeedsRes retrieveFeeds(FeedDto.GetFeedsReq getFeedsReq) {
    //TODO: redis 사용하는 방식으로 변경할 예정
    validate(getFeedsReq);
    Long count = feedRepository.countFeedInfos(getFeedsReq);
    List<FeedDto.FeedRawInfo> feedRawInfoList = feedRepository.findFeedInfos(getFeedsReq);
    return FeedDto.GetFeedsRes.builder()
        .count(count)
        .feeds(makeFeedInfos(feedRawInfoList))
        .build();
  }

  @Override
  @Transactional
  public FeedDto.PutFeedsRes editFeeds(FeedDto.PutFeedsReq putFeedsReq, UserInfo userInfo) {
    validate(putFeedsReq);
    Feed feed = feedRepository.findFeedById(putFeedsReq.getFeedId()).orElseThrow(() -> new BoardException(NON_EXIST_FEED));
    if (!feed.getUserInfo().getId().equals(userInfo.getId())) {
      throw new BoardException(NOT_MY_FEED);
    }
    feed.edit(putFeedsReq.getContent());
    return FeedDto.PutFeedsRes.builder()
        .feedId(feed.getId())
        .build();
  }

  @Override
  @Transactional
  public FeedDto.DeleteFeedsRes deleteFeeds(FeedDto.DeleteFeedsReq deleteFeedsReq, UserInfo userInfo) {
    validate(deleteFeedsReq);
    Feed feed = feedRepository.findFeedById(deleteFeedsReq.getFeedId()).orElseThrow(() -> new BoardException(NON_EXIST_FEED));
    if (!feed.getUserInfo().getId().equals(userInfo.getId())) {
      throw new BoardException(NOT_MY_FEED);
    }
    feed.delete();
    return FeedDto.DeleteFeedsRes.builder()
        .feedId(feed.getId())
        .build();
  }

  @Transactional
  @Override
  public FeedDto.GetFeedRes retrieveFeed(Long feedId, UserInfo userInfo) {
    validate(feedId);
    // TODO: 추후 조회수 증가 로직은 redis로 변경 예정
    Feed feed = retrieveFeedEntity(feedId);
    feed.increaseViewCnt();
    return makeGetFeedRes(feed);
  }

  @Transactional
  @Override
  public FeedDto.PutFeedBookmarkStatusRes editFeedBookmarkStatus(Long feedId, UserInfo userInfo) {
    validate(feedId);
    Feed feed = retrieveFeedEntity(feedId);
    boolean changeStatus = bookmarkService.changeFeedBookmark(userInfo, feed);
    if (changeStatus) { // 즐겨 찾기 추가한 경우
      feed.increaseBookmarkCnt();
    } else { // 즐겨 찾기 해제한 경우
      feed.decreaseBookmarkCnt();
    }
    return FeedDto.PutFeedBookmarkStatusRes.builder()
        .feedId(feed.getId())
        .status(changeStatus)
        .build();
  }

  @Override
  public Feed retrieveFeedEntity(Long feedId) {
    return feedRepository.findFeedById(feedId).orElseThrow(() -> new BoardException(NON_EXIST_FEED));
  }

  /**
   * 피드 생성 서비스 유효성 검사
   */
  private void validate(FeedDto.PostFeedsReq postFeedsReq) {
    if (postFeedsReq.getContent() == null || postFeedsReq.getContent().isEmpty()) {
      throw new BoardException(NON_EXIST_FEED_CONTENT);
    }
  }

  /**
   * 피드 조회 및 검색 서비스 유효성 검사
   */
  private void validate(FeedDto.GetFeedsReq getFeedsReq) {
    if (getFeedsReq.getFeedId() == null) {
      throw new BoardException(NON_EXIST_FEED_ID);
    }
    if (getFeedsReq.getSize() == null) {
      throw new BoardException(NON_EXIST_PAGE_SIZE);
    }
  }

  /**
   * 피드 수정 서비스 유효성 검사
   */
  private void validate(FeedDto.PutFeedsReq putFeedsReq) {
    if (putFeedsReq.getFeedId() == null) {
      throw new BoardException(NON_EXIST_FEED_ID);
    }
    if (putFeedsReq.getContent() == null || putFeedsReq.getContent().isEmpty()) {
      throw new BoardException(NON_EXIST_FEED_CONTENT);
    }
  }

  /**
   * 피드 삭제 서비스 유효성 검사
   */
  private void validate(FeedDto.DeleteFeedsReq deleteFeedsReq) {
    if (deleteFeedsReq.getFeedId() == null) {
      throw new BoardException(NON_EXIST_FEED_ID);
    }
  }

  /**
   * 피드 아이디 존재 여부 체크
   */
  private void validate(Long feedId) {
    if (feedId == null) {
      throw new BoardException(NON_EXIST_FEED_ID);
    }
  }

  /**
   * List<FeedRawInfo> -> List<FeedInfo>로 변환해주는 메서드
   */
  public List<FeedDto.FeedInfo> makeFeedInfos(List<FeedDto.FeedRawInfo> feedRawInfos) {
    return feedRawInfos.stream().map(feedRawInfo -> FeedDto.FeedInfo.builder()
        .feedId(feedRawInfo.getId())
        .content(feedRawInfo.getContent())
        .userBasicInfo(UserDto.UserBasicInfo.builder()
            .userId(feedRawInfo.getUserId())
            .email(feedRawInfo.getEmail())
            .nickname(feedRawInfo.getNickname())
            .role(feedRawInfo.getRole())
            .build()
        )
        .viewCnt(feedRawInfo.getViewCnt())
        .commentCnt(feedRawInfo.getCommentCnt())
        .bookmarkCnt(feedRawInfo.getBookmarkCnt())
        .createdAt(feedRawInfo.getCreatedAt().atZone(ZoneId.systemDefault()).toEpochSecond())
        .updatedAt(feedRawInfo.getUpdatedAt().atZone(ZoneId.systemDefault()).toEpochSecond())
        .build()
    ).toList();
  }

  /**
   * FeedRawInfo -> GetFeedRes로 변환해주는 메서드
   */
  public FeedDto.GetFeedRes makeGetFeedRes(Feed feed) {
    return FeedDto.GetFeedRes.builder()
        .feedId(feed.getId())
        .content(feed.getContent())
        .userBasicInfo(UserDto.UserBasicInfo.builder()
            .userId(feed.getUserInfo().getId())
            .email(feed.getUserInfo().getEmail())
            .nickname(feed.getUserInfo().getNickname())
            .role(feed.getUserInfo().getRole())
            .build()
        )
        .viewCnt(feed.getViewCnt())
        .commentCnt(feed.getCommentCnt())
        .bookmarkCnt(feed.getBookmarkCnt())
        .createdAt(feed.getCreatedAt().atZone(ZoneId.systemDefault()).toEpochSecond())
        .updatedAt(feed.getUpdatedAt().atZone(ZoneId.systemDefault()).toEpochSecond())
        .build();
  }
}
