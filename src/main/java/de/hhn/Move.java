package de.hhn;

import de.hhn.lib.Vector2D;

public class Move {
    public final CharacterKind characterKind;
    public final Vector2D target;

    public Move (CharacterKind characterKind, Vector2D target){
        this.characterKind = characterKind;
        this.target = target;
    }
}
