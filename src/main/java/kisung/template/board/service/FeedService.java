package kisung.template.board.service;

import jakarta.transaction.Transactional;
import kisung.template.board.common.code.ErrorCode;
import kisung.template.board.config.exception.BoardException;
import kisung.template.board.dto.FeedDto;
import kisung.template.board.entity.Feed;
import kisung.template.board.entity.UserInfo;
import kisung.template.board.repository.feed.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static kisung.template.board.enums.Status.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
  private final FeedRepository feedRepository;

  /**
   * 피드 생성 서비스
   */
  @Transactional
  public FeedDto.PostFeedsRes createFeed(FeedDto.PostFeedsReq postFeedsReq, UserInfo userInfo) {
    validate(postFeedsReq);
    Feed feed = createFeedEntity(postFeedsReq.getContent(), userInfo);
    feed = feedRepository.save(feed);
    return FeedDto.PostFeedsRes.builder()
        .feedId(feed.getId())
        .build();
  }

  /**
   * 피드 생성 서비스 유효성 검사
   */
  private void validate(FeedDto.PostFeedsReq postFeedsReq) {
    if (postFeedsReq.getContent() == null || postFeedsReq.getContent().isEmpty()) {
      throw new BoardException(ErrorCode.NON_EXIST_CONTENT);
    }
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
