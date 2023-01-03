package de.hhn.lib;

/**
 * Stellt eine zweidimensionale Richtungsangabe dar.
 */
public record Vector2D(int x, int y) {
    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x(), this.y + other.y());
    }
}
