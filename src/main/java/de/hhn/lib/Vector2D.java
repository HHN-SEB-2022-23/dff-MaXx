package de.hhn.lib;

import java.util.Objects;

/** Stellt eine zweidimensionale Richtungsangabe dar. */
public final class Vector2D {
  private final int x;
  private final int y;

  /** */
  public Vector2D(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  public Vector2D add(final Vector2D other) {
    return new Vector2D(this.x + other.x(), this.y + other.y());
  }

  public Vector2D relativeTo(final Vector2D other) {
    return new Vector2D(this.x - other.x(), this.y - other.y());
  }

  public int x() {
    return this.x;
  }

  public int y() {
    return this.y;
  }

  @Override
  public boolean equals(final Object obj) {
    return obj instanceof final Vector2D other && this.x == other.x() && this.y == other.y();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
  }

  @Override
  public String toString() {
    return String.format("[%d, %d]", this.x, this.y);
  }
}