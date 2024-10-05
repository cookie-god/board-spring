package kisung.template.board.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {
  @Column(name="created_at")
  private LocalDateTime createdAt;
  @Column(name="updated_at")
  private LocalDateTime  updatedAt;
  @Column(name="status")
  private String status;
}
