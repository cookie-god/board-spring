package kisung.template.board.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import kisung.template.board.enums.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@MappedSuperclass
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {
  @Column(name="created_at")
  private LocalDateTime createdAt;
  @Column(name="updated_at")
  private LocalDateTime  updatedAt;
  @Column(name="status")
  private String status;

  protected void changeUpdatedAt() {
    this.updatedAt = LocalDateTime.now();
  }

  protected void changeStatus(Status status) {
    this.status = status.value();
  }
}
