package de.hhn;

import de.hhn.services.RandomFractionService;

public class Field implements ReadOnlyField {

    private static final RandomFractionService rfs = new RandomFractionService();
    private Fraction value;

    public Field() {
        this.value = Field.rfs.nextFraction();
    }

    @Override
    public String toString() {
        return this.value.compareTo(Fraction.ZERO) == 0 ? "" : this.value.toString();
    }

    public void setRandomValue() {
        this.value = Field.rfs.nextFraction();
    }

    public void setZero() {
        this.value = Fraction.ZERO;
    }
}
