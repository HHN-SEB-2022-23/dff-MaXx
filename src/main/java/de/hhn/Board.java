package de.hhn;

import de.hhn.lib.DoublyLinkedList;

public class Board {
    public final Character characterB;
    public final Character characterW;
    public final DoublyLinkedList<Field> fields;

    public Board() {
        Field[][] fieldsMatrix = new Field[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                fieldsMatrix[i][j] = new Field();
            }
        }

        this.fields = DoublyLinkedList.from(fieldsMatrix);
        this.characterB = new Character();
        this.characterW = new Character();
    }
}
