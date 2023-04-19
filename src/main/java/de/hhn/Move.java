package de.hhn;

import de.hhn.lib.Vector2D;

/**
 * Beschreibt einen Spielzug eines Spielers.
 */
public class Move {
    public final CharacterKind characterKind;
    public final Vector2D target;

    public Move(CharacterKind characterKind, Vector2D target) {
        this.characterKind = characterKind;
        this.target = target;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.characterKind, this.target);
    }
}
