package kisung.template.board.entity;

import jakarta.persistence.*;
import kisung.template.board.entity.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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

  @Column(name = "comment_cnt")
  private Long commentCnt;

  @Column(name = "bookmark_cnt")
  private Long bookmarkCnt;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserInfo userInfo;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "feed")
  private List<Comment> comments = new ArrayList<>();

  public void editFeed(String content) {
    this.content = content;
    this.changeUpdatedAt();
  }

  public void delete() {
    this.changeStatus(INACTIVE);
  }

  public void increaseCommentCnt() {
    this.commentCnt += 1;
  }
}
