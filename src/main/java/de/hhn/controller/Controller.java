package de.hhn.controller;

import de.hhn.model.Board;
import de.hhn.model.CharacterKind;
import de.hhn.model.Fraction;
import de.hhn.model.Move;
import de.hhn.services.SaveGameService;
import de.hhn.view.ActionListeners;
import de.hhn.view.GameField;
import de.hhn.view.GameScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import javax.swing.SwingUtilities;

/** Vermittelt zwischen Model (Board) und View (GameScreen). */
public class Controller extends Thread implements ActionListener {

  private final boolean load;
  private final Object lock = new Object();
  private final GameScreen view;
  private final Board model;

  private Move currentMove;
  private boolean running = true;
  private CharacterKind currentPlayer;

  public Controller(final boolean shouldLoadSaveGame) {
    final var wa =
        new WindowAdapter() {
          @Override
          public void windowClosing(final java.awt.event.WindowEvent e) {
            Controller.this.dispose();
          }
        };
    this.model = new Board();
    this.view = new GameScreen(this, wa, new ActionListeners(this));
    this.currentPlayer = CharacterKind.BLACK;
    this.load = shouldLoadSaveGame;
  }

  // clean up resources
  public void dispose() {
    this.view.dispose();
    this.running = false;
    this.currentMove = null;
    synchronized (this.lock) {
      this.lock.notify();
    }
  }

  // Button click from UI
  @Override
  public void actionPerformed(final ActionEvent e) {
    if (e.getSource() instanceof final GameField fieldEl) {
      if (!fieldEl.isEnabled()) {
        return;
      }

      final var pos = fieldEl.getPosition();
      this.currentMove =
          new Move(
              this.currentPlayer,
              pos.relativeTo(this.model.getCharacter(this.currentPlayer).getPosition()));

      SwingUtilities.invokeLater(
          () -> {
            synchronized (this.lock) {
              this.lock.notify();
            }
          });
    }
  }

  // start the game loop
  @Override
  public void run() {
    if (this.load) {
      if (!SaveGameService.load(this)) {
        this.dispose();
        return;
      }
    }

    while (this.running) {
      this.view.draw(
          this.model.fields,
          this.model.characterB,
          this.model.characterW,
          this.currentPlayer);

      // Warte aus UI Eingabe (ui thread)
      synchronized (this.lock) {
        try {
          this.lock.wait();
        } catch (final InterruptedException ignore) {
          System.err.println("Interrupted while waiting for move.");
          break;
        }
      }

      if (this.currentMove == null) {
        continue;
      }

      this.updateModel(this.currentMove);
      this.checkKill(this.currentMove.characterKind);
      if (this.checkWinner()) {
        this.view.dispose();
        break;
      }

      this.currentMove = null;
      this.currentPlayer = this.currentPlayer.getOpposite();
    }
  }

  // check if a player has won
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

  // check if a player has killed another player
  private void checkKill(final CharacterKind maybeKiller) {
    final var killerCharacter = this.model.getCharacter(maybeKiller);
    final var targetCharacter = this.model.getCharacter(maybeKiller.getOpposite());

    if (killerCharacter.getPosition().equals(targetCharacter.getPosition())) {
      killerCharacter.incrementPoints(targetCharacter.getPoints());
      targetCharacter.resetPoints();
      targetCharacter.teleport(this.model.getStartFieldFor(targetCharacter.getKind()));
    }
  }

  // update the model based on a move
  private void updateModel(final Move move) {
    final var character = this.model.getCharacter(move.characterKind);

    character.move(move.target);
  }

  public Board getBoard() {
    return this.model;
  }

  public CharacterKind getCurrentPlayer() {
    return this.currentPlayer;
  }

  public void setCurrentPlayer(CharacterKind currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public GameScreen getGameScreen() {
    return this.view;
  }
}