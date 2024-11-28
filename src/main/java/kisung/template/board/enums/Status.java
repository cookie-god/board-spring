package kisung.template.board.enums;

public enum Status {
  ACTIVE("ACTIVE"),
  INACTIVE("INACTIVE");

  String status;

  Status(String status) {
    this.status = status;
  }

  public String value() {
    return status;
  }
}
