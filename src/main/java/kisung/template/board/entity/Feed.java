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
@Table(name = "FEED")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString
public class Feed extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob
  @Column(name = "content", columnDefinition = "TEXT")
  private String content;

  @Column(name = "view_cnt")
  private Long viewCnt;

  @Column(name = "comment_cnt")
  private Long commentCnt;

  @Column(name = "bookmark_cnt")
  private Long bookmarkCnt;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserInfo userInfo;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "feed")
  private List<Comment> comments = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "feed")
  private List<FeedBookmark> feedBookmarks = new ArrayList<>();

  /**
   * 피드 엔티티 생성 정적 팩토리 메서드
   */
  public static Feed of(String content, UserInfo userInfo) {
    LocalDateTime now = LocalDateTime.now();
    return Feed.builder()
      .content(content)
      .viewCnt(0L)
      .commentCnt(0L)
      .bookmarkCnt(0L)
      .userInfo(userInfo)
      .createdAt(now)
      .updatedAt(now)
      .status(ACTIVE.value())
      .build();
  }

  public void edit(String content) {
    this.content = content;
    this.changeUpdatedAt();
  }

  public void delete() {
    this.changeStatus(INACTIVE);
  }

  public void increaseCommentCnt() {
    this.commentCnt += 1;
  }

  public void increaseViewCnt() {
    this.viewCnt += 1;
  }
  public void increaseBookmarkCnt() {
    this.bookmarkCnt += 1;
  }
  public void decreaseBookmarkCnt() {
    this.bookmarkCnt -= 1;
  }
}
