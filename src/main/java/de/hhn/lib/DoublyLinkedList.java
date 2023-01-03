package de.hhn.lib;

import org.w3c.dom.Node;

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
        for (int i = 0; i < position.x(); i++) {
            curr = curr.getEast().orElseThrow();
        }
        for (int i = 0; i < position.y(); i++) {
            curr = curr.getSouth().orElseThrow();
        }
        return curr;
    }

    protected static <U> DoublyLinkedListNode<U> constructFromMatrix(U arr[][], int i, int j, int m, int n, DoublyLinkedListNode<U>[][] visited) {
        // return if i or j is out of bounds
        if (( i > ( m - 1 ) ) || ( j > ( n - 1 ) ) || ( i < 0 ) || ( j < 0 )) {
            return null;
        }

        // Check if node is previously created then,
        // don't need to create new
        if (visited[i][j] != null) {
            return visited[i][j];
        }

        // create a new node for current i and j
        // and recursively allocate its down and
        // right pointers
        var temp = new DoublyLinkedListNode(arr[i][j], i == 0 && j == 0);
        visited[i][j] = temp;
        temp.setEast(DoublyLinkedList.constructFromMatrix(arr, i, j + 1, m, n, visited));
        temp.setSouth(DoublyLinkedList.constructFromMatrix(arr, i + 1, j, m, n, visited));
        temp.setWest(DoublyLinkedList.constructFromMatrix(arr, i, j - 1, m, n, visited));
        temp.setNorth(DoublyLinkedList.constructFromMatrix(arr, i - 1, j, m, n, visited));
        return temp;
    }

    public static <T> DoublyLinkedList<T> from(T[][] matrix) {
        if (matrix.length == 0) {
            throw new IllegalArgumentException("Matrix must not be empty.");
        }

        int m = matrix.length;
        int n = matrix[0].length;

        DoublyLinkedListNode<T>[][] visited = new DoublyLinkedListNode[m][n];

        var anchor = DoublyLinkedList.constructFromMatrix(matrix, 0, 0, m, n, visited);

        return new DoublyLinkedList<>(anchor);
    }

    public DoublyLinkedListNode<T> getAnchor() {
        return this.anchor;
    }
}
