package de.hhn.model;

import de.hhn.lib.DoublyLinkedList;
import de.hhn.lib.DoublyLinkedListNode;
import de.hhn.lib.Vector2D;

/**
 * Das Spielbrett. Initialisiert und hält alle Daten die für das Spiel benötigt
 * werden.
 */
public class Board {
  public static final Vector2D startBlack = new Vector2D(4, 5);
  public static final Vector2D startWhite = new Vector2D(3, 2);

  public static Vector2D getStartPositionFor(final CharacterKind kind) {
    if (kind == CharacterKind.BLACK) {
      return Board.startBlack;
    }

    return Board.startWhite;
  }

  public final Character characterB;
  public final Character characterW;

  public final DoublyLinkedList<Field> fields;

  public Board() {
    final Field[][] fieldsMatrix = new Field[8][8];
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        fieldsMatrix[i][j] = new Field(new Vector2D(j, i));
      }
    }

    this.fields = DoublyLinkedList.from(fieldsMatrix);

    this.characterB = new Character(CharacterKind.BLACK, this.fields.getAt(Board.startBlack));

    this.characterW = new Character(CharacterKind.WHITE, this.fields.getAt(Board.startWhite));
  }

  public Character getCharacter(final CharacterKind kind) {
    if (kind == CharacterKind.BLACK) {
      return this.characterB;
    }

    return this.characterW;
  }

  public DoublyLinkedListNode<Field> getStartFieldFor(final CharacterKind kind) {
    return this.fields.getAt(Board.getStartPositionFor(kind));
  }
}