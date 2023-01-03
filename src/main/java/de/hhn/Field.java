package de.hhn;

import de.hhn.services.RandomFractionService;

/**
 * Die Daten eines Feldes auf dem Spielbrett.
 *
 * Verwaltet nur die darin enthaltene Fraction.
 */
public class Field implements ReadOnlyField {

    private static final RandomFractionService rfs = new RandomFractionService();
    private Fraction value;

    public Field() {
        this.value = Field.rfs.nextFraction();
    }

    @Override
    public String toString() {
        if (this.value.compareTo(Fraction.ZERO) == 0) {
            return "";
        }

        return this.value.toString();
    }

    public void setRandomValue() {
        this.value = Field.rfs.nextFraction();
    }

    public Fraction getValue() {
        return this.value;
    }

    public void setZero() {
        this.value = Fraction.ZERO;
    }
}
