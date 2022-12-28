package de.hhn;

import de.hhn.services.RandomFractionService;

public class Field implements ReadOnlyField{

private Fraction value;

private static final RandomFractionService rfs = new RandomFractionService();


@Override
public String toString(){
    return value.compareTo(Fraction.ZERO) == 0 ? "" : value.toString();
}

public void setRandomValue () {
    value = rfs.nextFraction();


}

public void setZero(){
    value = Fraction.ZERO;
}

}
