package kisung.template.board.enums;

public enum Role {
  ADMIN("ADMIN"),
  USER("USER");

  String role;

  Role(String role) {
    this.role = role;
  }

  public String value() {
    return role;
  }
}
