package kisung.template.board.entity;

import jakarta.persistence.*;
import kisung.template.board.entity.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Lazy;


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

  public void editFeed(String content, UserInfo userInfo) {
    this.content = content;
    this.userInfo = userInfo;
    this.changeUpdatedAt();
  }
}
