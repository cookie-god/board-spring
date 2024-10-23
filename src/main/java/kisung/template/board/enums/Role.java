package kisung.template.board.enums;

public enum Role {
  ROLE_ADMIN("ROLE_ADMIN"),
  ROLE_USER("ROLE_USER");

  String role;

  Role(String role) {
    this.role = role;
  }

  public String value() {
    return role;
  }
}
