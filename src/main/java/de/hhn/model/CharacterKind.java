package de.hhn.model;

/** Die möglichen Figurenarten. */
public enum CharacterKind {
  BLACK("Black"),
  WHITE("White");

  private final String displayName;

  CharacterKind(final String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return this.displayName;
  }

  public CharacterKind getOpposite() {
    return switch (this) {
      case BLACK -> CharacterKind.WHITE;
      case WHITE -> CharacterKind.BLACK;
    };
  }
}