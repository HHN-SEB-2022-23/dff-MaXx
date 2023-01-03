package de.hhn;

/**
 * Vermittelt zwischen Model (Board) und View (GameScreen).
 */
public class Controller {
    private final GameScreen view;
    private final Board model;
    private CharacterKind currentPlayer;

    public Controller() {
        this.view = new GameScreen();
        this.model = new Board();
        this.currentPlayer = CharacterKind.WHITE;
    }

    public void start() {
        gameloop:
        while (true) {
            this.view.draw(
                this.model.fields,
                this.model.characterB,
                this.model.characterW,
                this.currentPlayer = this.currentPlayer.getOpposite()
            );

            var move = this.view.getNextMove();
            if (move.isEmpty()) {
                break gameloop;
            }

            this.updateModel(move.get());

            if (this.model.characterB.getPoints().compareTo(Fraction.FIFTY_THREE) > 0) {
                this.view.drawWinner(this.model.characterB);
                break gameloop;
            }

            if (this.model.characterW.getPoints().compareTo(Fraction.FIFTY_THREE) > 0) {
                this.view.drawWinner(this.model.characterW);
                break gameloop;
            }
        }

        System.out.println("Spiel beendet");
    }

    private void updateModel(Move move) {
        var character = this.currentPlayer == CharacterKind.WHITE
            ? this.model.characterW
            : this.model.characterB;

        character.move(move.target);
    }
}