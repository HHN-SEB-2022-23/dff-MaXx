package de.hhn;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Vermittelt zwischen Model (Board) und View (GameScreen).
 */
public class Controller extends Thread implements ActionListener {
    private static final Object lock = new Object();
    private final GameScreen view;
    private final Board model;
    private CharacterKind currentPlayer;

    public Controller() {
        this.view = new GameScreen(this);
        this.model = new Board();
        this.currentPlayer = CharacterKind.WHITE;
    }

    private static Move currentMove;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof GameScreen.GameField fieldEl) {
            if (!fieldEl.isEnabled()) {
                return;
            }

            var pos = fieldEl.getPosition();
            Controller.currentMove = new Move(this.currentPlayer,
                pos.relativeTo(this.model.getCharacter(this.currentPlayer).getPosition())
            );

            SwingUtilities.invokeLater(() -> {
                synchronized (Controller.lock) {
                    Controller.lock.notify();
                }
            });
        }
    }

    @Override
    public void run() {
        while (true) {
            this.view.draw(this.model.fields,
                this.model.characterB,
                this.model.characterW,
                this.currentPlayer = this.currentPlayer.getOpposite()
            );

            // Warte aus UI Eingabe (main thread)
            synchronized (Controller.lock) {
                try {
                    Controller.lock.wait();
                } catch (InterruptedException ignore) {
                    System.err.println("Interrupted while waiting for user input.");
                    System.exit(1);
                }
            }

            this.updateModel(Controller.currentMove);
            this.checkKill(Controller.currentMove.characterKind);
            if (this.checkWinner()) {
                System.exit(0);
            }
        }
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
            targetCharacter.teleport(this.model.getStartFieldFor(targetCharacter.getKind()));
        }
    }

    private void updateModel(Move move) {
        var character = this.model.getCharacter(move.characterKind);

        character.move(move.target);
    }
}
