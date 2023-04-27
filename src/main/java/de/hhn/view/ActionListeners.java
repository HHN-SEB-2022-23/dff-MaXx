package de.hhn.view;

import de.hhn.controller.Controller;
import de.hhn.services.SaveGameService;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collections;

/** ActionListeners for the buttons in the UI. */
public class ActionListeners {
  private final Controller controller;

  public ActionListeners(final Controller controller) {
    this.controller = controller;
  }

  public void ourTeam(final ActionEvent ignore) {
    final var contributors =
        Arrays.asList("Frank Mayer", "René Ott", "Antonia Friese", "Felix Marzioch");
    Collections.shuffle(contributors);
    MessageDialog.show("Credits", String.join(", ", contributors));
  }

  public void manual(final ActionEvent ignore) {
    final var manualTxt =
        """
        MaXx is a board game for two players. The game is played on a 8x8 board.
        The goal of the game is to have more stones on the board than the opponent.
        The game ends when all fields are occupied or no more moves are possible.
        The game starts with 2 stones of each player in the middle of the board.
        The players take turns placing their stones on the board.
        A stone can only be placed on a field that is adjacent to a stone of the opponent.
        If a stone is placed on a field that is adjacent to a stone of the opponent,
        all stones of the opponent that are in a straight line between the placed stone
        and another stone of the player are turned over.
        """;
    MessageDialog.show("Manual", manualTxt);
  }

  public void loadGame(final ActionEvent ignore) {
    SaveGameService.load(this.controller);
    this.controller
        .getGameScreen()
        .draw(
            this.controller.getBoard().fields,
            this.controller.getBoard().characterB,
            this.controller.getBoard().characterW,
            this.controller.getCurrentPlayer());
  }

  public void saveGame(final ActionEvent ignore) {
    SaveGameService.save(this.controller);
  }
}