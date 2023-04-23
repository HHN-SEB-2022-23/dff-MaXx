package de.hhn.model;

import de.hhn.lib.Vector2D;

/**
 * Interface für ein Field, für die Oberfläche, die am Field nichts ändern darf.
 */
public interface ReadOnlyField {
  String toString();

  Vector2D getPosition();

  Fraction getValue();
}