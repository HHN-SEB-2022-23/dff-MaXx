package de.hhn.lib;

/**
 * Stellt eine zweidimensionale Richtungsangabe dar.
 */
public class Vector2D {
    int x;
    int y;

    public Vector2D (int x, int y) {
        this.y = y;
        this.x = x;

    }

    public int getX () {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }
}
