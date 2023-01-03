package de.hhn;

import de.hhn.lib.Vector2D;

/**
 * Interface für den Character, für die Oberfläche, die am Character nichts ändern darf.
 */
public interface ReadOnlyCharacter {
    Fraction getPoints();
    Vector2D getPosition();
    String toString();
}
