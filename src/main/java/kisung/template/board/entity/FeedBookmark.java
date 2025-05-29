package kisung.template.board.entity;

import jakarta.persistence.*;
import kisung.template.board.entity.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static kisung.template.board.enums.Status.ACTIVE;
import static kisung.template.board.enums.Status.INACTIVE;


@Entity
@Table(name = "FEED_BOOKMARK")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString
public class FeedBookmark extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserInfo userInfo;

  @ManyToOne
  @JoinColumn(name = "feed_id")
  private Feed feed;

  /**
   * 피드 북마크 엔티티 생성 정적 팩토리 메서드
   */
  public static FeedBookmark of(UserInfo userInfo, Feed feed) {
    LocalDateTime now = LocalDateTime.now();
    return FeedBookmark.builder()
      .userInfo(userInfo)
      .feed(feed)
      .createdAt(now)
      .updatedAt(now)
      .status(ACTIVE.value())
      .build();
  }
}
