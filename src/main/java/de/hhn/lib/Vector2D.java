package de.hhn.lib;

import java.util.Objects;

/**
 * Stellt eine zweidimensionale Richtungsangabe dar.
 */
public final class Vector2D {
    private final int x;
    private final int y;

    /**
     *
     */
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x(), this.y + other.y());
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Vector2D) obj;
        return this.x == that.x() &&
            this.y == that.y();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
