package de.hhn;

import de.hhn.lib.Vector2D;

public class Character implements ReadOnlyCharacter {
    private Fraction points;
    private Vector2D position;

    public void incrementPoints(Fraction points) {
        this.points = this.points.add(points);
    }

    @Override
    public Fraction getPoints() {
        return this.points;
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }
}
