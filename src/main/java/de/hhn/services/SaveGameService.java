package de.hhn.services;

import de.hhn.controller.Controller;
import de.hhn.lib.Vector2D;
import de.hhn.model.CharacterKind;
import de.hhn.model.Fraction;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class SaveGameService {

  private static class SaveGameData implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int NUMBER_RADIX = 16;
    String[][] gameField = new String[64][2];
    int[][] playerPos = new int[2][2];
    String[][] playerPoints = new String[2][2];
    boolean currentPlayerIsBlack;

    public SaveGameData(final Controller controller) {
      final var board = controller.getBoard();

      // game field
      var rowStartFld = board.fields.getAnchor();
      var fld = rowStartFld;
      for (int i = 0; i < 64; i++) {
        final var frac = fld.getData().getValue();
        gameField[i][0] = frac.getNumerator().toString(NUMBER_RADIX);
        gameField[i][1] = frac.getDenominator().toString(NUMBER_RADIX);
        final var fldEast = fld.getEast();
        if (fldEast.isPresent()) {
          fld = fldEast.get();
        } else {
          final var fldSouth = rowStartFld.getSouth();
          if (fldSouth.isPresent()) {
            fld = fldSouth.get();
            rowStartFld = fld;
          } else {
            break;
          }
        }
      }

      // player positions
      final var posB = board.characterB.getPosition();
      final var posW = board.characterW.getPosition();
      this.playerPos[0][0] = posB.x();
      this.playerPos[0][1] = posB.y();
      this.playerPos[1][0] = posW.x();
      this.playerPos[1][1] = posW.y();

      // player points
      final var pointsB = board.characterB.getPoints();
      final var pointsW = board.characterW.getPoints();
      this.playerPoints[0][0] = pointsB.getNumerator().toString(NUMBER_RADIX);
      this.playerPoints[0][1] = pointsB.getDenominator().toString(NUMBER_RADIX);
      this.playerPoints[1][0] = pointsW.getNumerator().toString(NUMBER_RADIX);
      this.playerPoints[1][1] = pointsW.getDenominator().toString(NUMBER_RADIX);

      // current player
      this.currentPlayerIsBlack = controller.getCurrentPlayer() == CharacterKind.BLACK;
    }

    public void loadInto(final Controller controller) {
      final var board = controller.getBoard();

      // game field
      var rowStartFld = board.fields.getAnchor();
      var fld = rowStartFld;
      for (int i = 0; i < 64; i++) {
        final var frac =
            new Fraction(
                new BigInteger(gameField[i][0], SaveGameData.NUMBER_RADIX),
                new BigInteger(gameField[i][1], SaveGameData.NUMBER_RADIX));
        fld.getData().setValue(frac);
        final var fldEast = fld.getEast();
        if (fldEast.isPresent()) {
          fld = fldEast.get();
        } else {
          final var fldSouth = rowStartFld.getSouth();
          if (fldSouth.isPresent()) {
            fld = fldSouth.get();
            rowStartFld = fld;
          } else {
            break;
          }
        }
      }

      // player positions
      final var posB = new Vector2D(this.playerPos[0][0], this.playerPos[0][1]);
      final var posW = new Vector2D(this.playerPos[1][0], this.playerPos[1][1]);
      board.characterB.teleport(posB);
      board.characterW.teleport(posW);

      // player points
      final var pointsB =
          new Fraction(
              new BigInteger(this.playerPoints[0][0], SaveGameData.NUMBER_RADIX),
              new BigInteger(this.playerPoints[0][1], SaveGameData.NUMBER_RADIX));
      final var pointsW =
          new Fraction(
              new BigInteger(this.playerPoints[1][0], SaveGameData.NUMBER_RADIX),
              new BigInteger(this.playerPoints[1][1], SaveGameData.NUMBER_RADIX));
      board.characterB.resetPoints();
      board.characterB.incrementPoints(pointsB);
      board.characterW.resetPoints();
      board.characterW.incrementPoints(pointsW);

      // current player
      controller.setCurrentPlayer(
          this.currentPlayerIsBlack ? CharacterKind.BLACK : CharacterKind.WHITE);
    }
  }

  private static final String SAVE_GAME_FILE = "./MaXx_SaveGame";

  public static void save(final Controller controller) {
    try {
      final var data = new SaveGameData(controller);
      final var out = new FileOutputStream(SaveGameService.SAVE_GAME_FILE);
      final var zip = new DeflaterOutputStream(out);
      final var oos = new ObjectOutputStream(zip);
      oos.writeObject(data);
      oos.flush();
      oos.close();
    } catch (final Exception e) {
      System.err.println("Error while writing to file: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public static void load(final Controller controller) {
    final var f = new File(SaveGameService.SAVE_GAME_FILE);
    if (!f.exists() || f.isDirectory()) {
      System.err.println("No save game file found.");
      return;
    }

    try {
      final var fileIn = new FileInputStream(f);
      final var zipIn = new InflaterInputStream(fileIn);
      final var ois = new ObjectInputStream(zipIn);
      final var data = (SaveGameData) ois.readObject();
      ois.close();
      fileIn.close();
      data.loadInto(controller);
    } catch (final Exception e) {
      System.err.println("Error while reading from file: " + e.getMessage());
      e.printStackTrace();
    }
  }
}