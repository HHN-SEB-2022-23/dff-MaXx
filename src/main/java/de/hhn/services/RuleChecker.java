package de.hhn.services;

import de.hhn.lib.Vector2D;
import de.hhn.model.CharacterKind;
import java.util.Set;

/** Prüft, ob ein Spielzug gültig ist. */
public class RuleChecker {

  private static final Set<Vector2D> allMovesForBlack = Set.of(
      new Vector2D(1, 0),
      new Vector2D(-1, 0),
      new Vector2D(0, 1),
      new Vector2D(0, -1),
      new Vector2D(-1, 1));

  private static final Set<Vector2D> allMovesForWhite = Set.of(
      new Vector2D(1, 0),
      new Vector2D(-1, 0),
      new Vector2D(0, 1),
      new Vector2D(0, -1),
      new Vector2D(1, -1));

  public static Set<Vector2D> getAllMovesFor(final CharacterKind characterKind) {
    switch (characterKind) {
      case WHITE -> {
        return RuleChecker.allMovesForWhite;
      }
      case BLACK -> {
        return RuleChecker.allMovesForBlack;
      }
      default -> {
        return null;
      }
    }
  }
}