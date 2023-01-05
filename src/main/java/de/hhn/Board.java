package de.hhn;

import de.hhn.lib.DoublyLinkedList;
import de.hhn.lib.Vector2D;

/**
 * Das Spielbrett.
 * Initialisiert und hält alle Daten die für das Spiel benötigt werden.
 */
public class Board {
    public final Character characterB;
    public final Character characterW;
    public final DoublyLinkedList<Field> fields;

    public Board() {
        Field[][] fieldsMatrix = new Field[ 8 ][ 8 ];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                fieldsMatrix[ i ][ j ] = new Field();
            }
        }

        this.fields = DoublyLinkedList.from(fieldsMatrix);

        this.characterB = new Character(
            CharacterKind.BLACK,
            this.fields.getAt(new Vector2D(4, 5)),
            new Vector2D(4, 5)
        );

        this.characterW = new Character(
            CharacterKind.WHITE,
            this.fields.getAt(new Vector2D(3, 2)),
            new Vector2D(3, 2)
        );
    }
}
