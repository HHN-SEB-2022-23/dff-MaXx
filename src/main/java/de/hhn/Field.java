package de.hhn;

import de.hhn.lib.Vector2D;
import de.hhn.services.RandomFractionService;

/**
 * Die Daten eines Feldes auf dem Spielbrett.
 *
 * <p>Verwaltet nur die darin enthaltene Fraction.
 */
public class Field implements ReadOnlyField {

  private static final RandomFractionService rfs = new RandomFractionService();
  private Fraction value;
  public final Vector2D position;

  public Field(Vector2D position) {
    this.value = Field.rfs.nextFraction();
    this.position = position;
  }

  @Override
  public String toString() {
    if (this.value.compareTo(Fraction.ZERO) == 0) {
      return "";
    }

    return this.value.toString();
  }

  public Fraction getValue() {
    return this.value;
  }

  public void setZero() {
    this.value = Fraction.ZERO;
  }
}