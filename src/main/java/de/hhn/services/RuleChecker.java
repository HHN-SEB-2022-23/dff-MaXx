package de.hhn.services;

import de.hhn.CharacterKind;
import de.hhn.Move;

/**
 * Prüft, ob ein Spielzug gültig ist.
 */
public class RuleChecker {
    public boolean isValidMove(Move move) {
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
}
