package de.hhn.lib;

/**
 * Two-dimensional linked list.
 */
public class DoublyLinkedList<T> {
    private DoublyLinkedListNode<T> anchor;

    public DoublyLinkedList(DoublyLinkedListNode<T> anchor) {
        this.anchor = anchor;
    }

    public DoublyLinkedList(T anchorValue) {
        this.anchor = new DoublyLinkedListNode<>(anchorValue, true);
        this.anchor.setNorth(null);
        this.anchor.setSouth(null);
        this.anchor.setEast(null);
        this.anchor.setWest(null);
    }

    protected static <U> DoublyLinkedListNode<U> constructFromMatrix(U[][] mat, int i, int j, DoublyLinkedListNode<U> last) {
        var curr = new DoublyLinkedListNode<>(mat[i][j], j == 0 && i == 0);

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

    public DoublyLinkedListNode<T> getAt(Vector2D position) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void insertAt(Vector2D position, T value) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
