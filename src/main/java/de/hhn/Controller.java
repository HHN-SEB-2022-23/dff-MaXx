package de.hhn;

import de.hhn.services.RuleChecker;

import java.util.Optional;

/**
 * Vermittelt zwischen Model (Board) und View (GameScreen).
 */
public class Controller {
    private final GameScreen view;
    private final Board model;
    private CharacterKind currentPlayer;
    private final RuleChecker ruleChecker;

    public Controller() {
        this.view = new GameScreen();
        this.model = new Board();
        this.currentPlayer = CharacterKind.WHITE;
        this.ruleChecker = new RuleChecker();
    }

    private boolean gameLoop() {
        this.view.draw(
            this.model.fields,
            this.model.characterB,
            this.model.characterW,
            this.currentPlayer = this.currentPlayer.getOpposite()
        );


        while (true) {
            Optional<Move> moveOpt = this.view.getNextMove();

            if (moveOpt.isEmpty()) {
                return true;
            }

            var move = moveOpt.get();

            if (!this.ruleChecker.isValidMove(move)) {
                GameScreen.drawInvalidMove(move.characterKind);
                continue;
            }

            this.updateModel(move);
            return this.checkWinner();
        }
    }

    public void start() {
        boolean running = true;

        while (running) {
            running = ! this.gameLoop();
        }

        System.out.println("Spiel beendet");
    }

    private boolean checkWinner() {
        if (this.model.characterB.getPoints().compareTo(Fraction.FIFTY_THREE) > 0) {
            GameScreen.drawWinner(this.model.characterB);
            return true;
        }

        if (this.model.characterW.getPoints().compareTo(Fraction.FIFTY_THREE) > 0) {
            GameScreen.drawWinner(this.model.characterW);
            return true;
        }

        return false;
    }

    private void updateModel(Move move) {
        var character = this.model.getCharacter(move.characterKind);

        character.move(move.target);
    }
}
