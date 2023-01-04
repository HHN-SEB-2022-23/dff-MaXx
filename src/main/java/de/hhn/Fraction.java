package de.hhn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Mathematischer Bruch.
 */
public class Fraction extends Number implements Comparable<Fraction> {
    private final BigInteger numerator;
    private final BigInteger denominator;

    public static final Fraction ZERO = new Fraction(0,1);
    public static final Fraction FIFTY_THREE = new Fraction(53,1);

    public Fraction(Number numerator, Number denominator) {
        this(BigInteger.valueOf(numerator.longValue()), BigInteger.valueOf(denominator.longValue()));
    }

    public Fraction(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("Denominator must not be zero.");
        }

        var gcd = numerator.gcd(denominator);
        this.numerator = numerator.divide(gcd);
        this.denominator = denominator.divide(gcd);
    }

    public static Fraction parse(String s) throws NumberFormatException {
        int slash = s.indexOf('/');
        if (slash == -1) {
            return new Fraction(new BigInteger(s), BigInteger.ONE);
        }

        var a = new BigInteger(s.substring(0, slash).trim());
        var b = new BigInteger(s.substring(slash + 1).trim());

        if (b.equals(BigInteger.ZERO)) {
            throw new NumberFormatException("Invalid fraction: Denominator is zero.");
        }

        return new Fraction(a, b);
    }

    public static Fraction from(Number value) {
        double z = value.longValue();
        double n = 1;

        while (z != Math.floor(z)) {
            z *= 10;
            n *= 10;
        }

        return new Fraction(BigInteger.valueOf((long) z), BigInteger.valueOf((long) n));
    }

    public static Fraction from(Number numerator, Number denominator) {
        return new Fraction(numerator, denominator);
    }

    public BigInteger getNumerator() {
        return this.numerator;
    }

    public BigInteger getDenominator() {
        return this.denominator;
    }

    Fraction add(Fraction r) {
        if (this.numerator.equals(BigInteger.ZERO)) {
            return r;
        }

        return new Fraction(this.numerator.multiply(r.denominator).add(r.numerator.multiply(this.denominator)), this.denominator.multiply(r.denominator));
    }

    Fraction subtract(Fraction r) {
        return new Fraction(this.numerator.multiply(r.denominator).subtract(r.numerator.multiply(this.denominator)), this.denominator.multiply(r.denominator));
    }

    Fraction multiply(Fraction r) {
        return new Fraction(this.numerator.multiply(r.numerator), this.denominator.multiply(r.denominator));
    }

    Fraction divide(Fraction r) {
        return new Fraction(this.numerator.multiply(r.denominator), this.denominator.multiply(r.numerator));
    }

    @Override
    public String toString() {
        return this.numerator + "/" + this.denominator;
    }

    boolean isInteger() {
        return this.numerator.remainder(this.denominator).equals(BigInteger.ZERO);
    }

    /**
     * Returns the value of the specified number as an {@code int}.
     *
     * @return the numeric value represented by this object after conversion
     * to type {@code int}.
     */
    @Override
    public int intValue() {
        return this.toBigDecimal().intValue();
    }

    /**
     * Returns the value of the specified number as a {@code long}.
     *
     * @return the numeric value represented by this object after conversion
     * to type {@code long}.
     */
    @Override
    public long longValue() {
        return this.toBigDecimal().longValue();
    }

    /**
     * Returns the value of the specified number as a {@code float}.
     *
     * @return the numeric value represented by this object after conversion
     * to type {@code float}.
     */
    @Override
    public float floatValue() {
        return this.toBigDecimal().floatValue();
    }

    /**
     * Returns the value of the specified number as a {@code double}.
     *
     * @return the numeric value represented by this object after conversion
     * to type {@code double}.
     */
    @Override
    public double doubleValue() {
        return this.toBigDecimal().doubleValue();
    }

    private BigDecimal toBigDecimal() {
        return new BigDecimal(this.numerator).divide(new BigDecimal(this.denominator), RoundingMode.HALF_UP);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * x.compareTo(y)} must throw an exception if and only if {@code
     * y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
     * == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     * @apiNote It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     */
    @Override
    public int compareTo(Fraction o) {
        BigDecimal a = this.toBigDecimal();
        BigDecimal b = o.toBigDecimal();

        return a.compareTo(b);
    }

    /**
     * Returns the hash code for this {@code Fraction}.
     */
    @Override
    public int hashCode() {
        return this.numerator.hashCode() ^ this.denominator.hashCode();
    }

    /**
     * Returns {@code true} if this {@code Fraction} is equal to the other object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Fraction other = (Fraction) o;
        return this.compareTo(other) == 0;
    }
}
