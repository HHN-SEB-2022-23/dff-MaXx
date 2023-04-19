package de.hhn.services;

import de.hhn.CharacterKind;
import de.hhn.Move;
import de.hhn.ReadOnlyCharacter;
import de.hhn.lib.Vector2D;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Prüft, ob ein Spielzug gültig ist.
 */
public class RuleChecker {
    private final static Set<Vector2D> allMovesForBlack = Set.of(
        new Vector2D(1, 0),
        new Vector2D(-1, 0),
        new Vector2D(0, 1),
        new Vector2D(0, -1),
        new Vector2D(-1, 1)
    );

    private final static Set<Vector2D> allMovesForWhite = Set.of(
        new Vector2D(1, 0),
        new Vector2D(-1, 0),
        new Vector2D(0, 1),
        new Vector2D(0, -1),
        new Vector2D(1, -1)
    );

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

        // SW
        return move.characterKind == CharacterKind.BLACK && move.target.x() == -1 && move.target.y() == 1;
    }

    private static boolean isOutOfBounds(Vector2D finalPosition) {
        return finalPosition.y() < 0 || finalPosition.y() > 7 || finalPosition.x() < 0 || finalPosition.x() > 7;
    }

    public static Set<Vector2D> getAllMovesFor(CharacterKind characterKind) {
        switch (characterKind) {
            case WHITE -> {
                return RuleChecker.allMovesForWhite;
            }
            case BLACK -> {
                return RuleChecker.allMovesForBlack;
            }
            default -> {
                return null;
            }
        }
    }
}
