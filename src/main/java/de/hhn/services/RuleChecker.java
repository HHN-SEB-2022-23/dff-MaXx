package de.hhn.services;

import de.hhn.CharacterKind;
import de.hhn.Move;
import de.hhn.ReadOnlyCharacter;
import de.hhn.lib.Vector2D;

/**
 * Prüft, ob ein Spielzug gültig ist.
 */
public class RuleChecker {
    public static boolean isValidMove(Move move, ReadOnlyCharacter character) {
        var finalPosition = character.getPosition().add(move.target);
        if (RuleChecker.isOutOfBounds(finalPosition)) {
            return false;
        }

        if (Math.abs(move.target.x()) + Math.abs(move.target.y()) == 1) {
            return true;
        }

        if (move.characterKind == CharacterKind.WHITE) {
            // NO
            return move.target.x() == 1 && move.target.y() == -1;
        }

        if (move.characterKind == CharacterKind.BLACK) {
            // SW
            return move.target.x() == -1 && move.target.y() == 1;
        }

        return false;
    }

    private static boolean isOutOfBounds(Vector2D finalPosition) {
        return finalPosition.y() < 0 || finalPosition.y() > 7 || finalPosition.x() < 0 || finalPosition.x() > 7;
    }
}
