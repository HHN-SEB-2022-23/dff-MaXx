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

    private void onMoveCallback(Move move) {
        System.out.println(move);
        this.updateModel(move);
        this.checkKill(move.characterKind);
        this.checkWinner();
        this.view.draw(this.model.fields, this.model.characterB,
            this.model.characterW,
            this.currentPlayer = this.currentPlayer.getOpposite()
        );
        this.view.getNextMove(this::onMoveCallback);
    }

    public void start() {
        this.view.draw(this.model.fields, this.model.characterB,
            this.model.characterW,
            this.currentPlayer = this.currentPlayer.getOpposite()
        );

        this.view.getNextMove(this::onMoveCallback);
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

    private void checkKill(CharacterKind maybeKiller) {
        var killerCharacter = this.model.getCharacter(maybeKiller);
        var targetCharacter = this.model.getCharacter(maybeKiller.getOpposite());

        if (killerCharacter.getPosition().equals(targetCharacter.getPosition())) {
            killerCharacter.incrementPoints(targetCharacter.getPoints());
            targetCharacter.resetPoints();
            targetCharacter.teleport(
                this.model.getStartFieldFor(targetCharacter.getKind()));
        }
    }

    private void updateModel(Move move) {
        var character = this.model.getCharacter(move.characterKind);

        character.move(move.target);
    }
}
