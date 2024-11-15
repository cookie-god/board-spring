package kisung.template.board.entity;

import jakarta.persistence.*;
import kisung.template.board.entity.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static kisung.template.board.enums.Status.INACTIVE;


@Entity
@Table(name = "COMMENT")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString
public class Comment extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "content", length = 300)
  private String content;

  @Column(name = "bookmark_cnt")
  private Long bookmarkCnt;

  @ManyToOne
  @JoinColumn(name = "parent_comment_id")
  private Comment parent;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserInfo userInfo;

  @ManyToOne
  @JoinColumn(name = "feed_id")
  private Feed feed;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
  private List<Comment> children = new ArrayList<>();

  public void editComment(String content) {
    this.content = content;
    this.changeUpdatedAt();
  }

  public void delete() {
    this.changeStatus(INACTIVE);
  }
}