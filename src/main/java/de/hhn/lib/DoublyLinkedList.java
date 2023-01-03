package de.hhn.lib;

/**
 * Two-dimensional linked list.
 *
 * Use the {@link #from(Object[][])} method to create a new instance.
 *
 * Da ein Zugriff per Index nicht notwendig ist, wird eine zweidimensionale Doubly Linked List verwendet.
 * Dabei wird eine Referenz auf ein Feld gehalten, von diesem Feld aus kommt man auf alle umliegenden Felder,
 * ohne wissen zu müssen an welcher Position auf dem Spielbrett sich das Feld befindet.
 */
public class DoublyLinkedList<T> {
    private DoublyLinkedListNode<T> anchor;

    public DoublyLinkedList(DoublyLinkedListNode<T> anchor) {
        this.anchor = anchor;
    }

    protected DoublyLinkedList(T anchorValue) {
        this.anchor = new DoublyLinkedListNode<>(anchorValue, true);
        this.anchor.setNorth(null);
        this.anchor.setSouth(null);
        this.anchor.setEast(null);
        this.anchor.setWest(null);
    }

    public DoublyLinkedListNode<T> getAt(Vector2D position) {
        var curr = this.anchor;
        for (int i = 0; i < position.getX(); i++) {
            curr = curr.getEast().orElseThrow();
        }
        for (int i = 0; i < position.getY(); i++) {
            curr = curr.getSouth().orElseThrow();
        }
        return curr;
    }

    protected static <U> DoublyLinkedListNode<U> constructFromMatrix(U[][] mat, int i, int j, DoublyLinkedListNode<U> last) {
        if (i >= mat.length || j >= mat[ i ].length) {
            return null;
        }

        var curr = new DoublyLinkedListNode<>(mat[ i ][ j ], j == 0 && i == 0);

        if (j == 0) {
            curr.setWest(null);
        }
        else {
            curr.setWest(last);
        }

        if (i == 0) {
            curr.setNorth(null);
        }
        else {
            curr.setNorth(last);
        }

        curr.setEast(DoublyLinkedList.constructFromMatrix(mat, i, j + 1, curr));
        curr.setSouth(DoublyLinkedList.constructFromMatrix(mat, i + 1, j, curr));

        return curr;
    }

    public static <T> DoublyLinkedList<T> from(T[][] matrix) {
        if (matrix.length == 0) {
            throw new IllegalArgumentException("Matrix must not be empty.");
        }

        var anchor = DoublyLinkedList.constructFromMatrix(matrix, 0, 0, null);

        return new DoublyLinkedList<>(anchor);
    }

    public DoublyLinkedListNode<T> getAnchor() {
        return this.anchor;
    }
}
