package kisung.template.board.service.feed;

import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.FeedDto;
import kisung.template.board.dto.UserDto;
import kisung.template.board.entity.Feed;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.repository.feed.FeedRepository;
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
public class FeedServiceImpl implements FeedService{
  private final FeedRepository feedRepository;

  /**
   * 피드 생성 서비스
   */
  @Override
  @Transactional
  public FeedDto.PostFeedsRes createFeeds(FeedDto.PostFeedsReq postFeedsReq, UserInfo userInfo) {
    validate(postFeedsReq);
    Feed feed = createFeedEntity(postFeedsReq.getContent(), userInfo);
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
      .feeds(makeFeedInfosByRawDatas(feedRawInfoList))
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
    feed.editFeed(putFeedsReq.getContent(), userInfo);
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

  /**
   * 피드 생성 서비스 유효성 검사
   */
  private void validate(FeedDto.PostFeedsReq postFeedsReq) {
    if (postFeedsReq.getContent() == null || postFeedsReq.getContent().isEmpty()) {
      throw new BoardException(NON_EXIST_CONTENT);
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
    if (putFeedsReq.getContent() == null || putFeedsReq.getContent().isEmpty()) {
      throw new BoardException(NON_EXIST_CONTENT);
    }
  }

  /**
   * 피드 수정 서비스 유효성 검사
   */
  private void validate(FeedDto.DeleteFeedsReq deleteFeedsReq) {
    if (deleteFeedsReq.getFeedId() == null) {
      throw new BoardException(NON_EXIST_FEED_ID);
    }
  }

  /**
   * FeedRawInfo -> FeedInfo로 변환해주는 메서드
   */
  public List<FeedDto.FeedInfo> makeFeedInfosByRawDatas(List<FeedDto.FeedRawInfo> feedRawInfos) {
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
      .commentCnt(feedRawInfo.getCommentCnt())
      .bookmarkCnt(feedRawInfo.getBookmarkCnt())
      .createdAt(feedRawInfo.getCreatedAt().atZone(ZoneId.systemDefault()).toEpochSecond())
      .updatedAt(feedRawInfo.getUpdatedAt().atZone(ZoneId.systemDefault()).toEpochSecond())
      .build()
    ).toList();
  }

  /**
   * 피드 엔티티 생성 메서드
   */
  private Feed createFeedEntity(String content, UserInfo userInfo) {
    LocalDateTime now = LocalDateTime.now();
    return Feed.builder()
        .content(content)
        .commentCnt(0L)
        .bookmarkCnt(0L)
        .userInfo(userInfo)
        .createdAt(now)
        .updatedAt(now)
        .status(ACTIVE.value())
        .build();
  }
}
