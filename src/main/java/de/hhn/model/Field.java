package de.hhn.model;

import de.hhn.lib.Vector2D;
import de.hhn.services.RandomFractionService;

/**
 * Die Daten eines Feldes auf dem Spielbrett.
 *
 * <p>
 * Verwaltet nur die darin enthaltene Fraction.
 */
public class Field implements ReadOnlyField {

  public final Vector2D position;
  private Fraction value;

  public Field(final Vector2D position) {
    this.value = RandomFractionService.nextFraction();
    this.position = position;
  }

  @Override
  public String toString() {
    if (this.value.compareTo(Fraction.ZERO) == 0) {
      return "";
    }

    return this.value.toString();
  }

  @Override
  public Vector2D getPosition() {
    return this.position;
  }

  public Fraction getValue() {
    return this.value;
  }

  public void setZero() {
    this.value = Fraction.ZERO;
  }
}