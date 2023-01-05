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
    private final DoublyLinkedListNode<T> anchor;

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

    protected static <U> DoublyLinkedListNode<U> constructFromMatrix(U[][] arr, int i, int j, int m, int n, DoublyLinkedListNode<U>[][] visited) {
        // abbruch, wenn i oder j außerhalb des Arrays liegt
        if (i > m - 1 || j > n - 1 || i < 0 || j < 0) {
            return null;
        }

        // Prüfe ob Node bereits erstellt wurde, wenn ja, dann gebe diese zurück
        if (visited[ i ][ j ] != null) {
            return visited[ i ][ j ];
        }

        // Erstelle neue Node für i und j rekursiv umliegende Referenzen setzen
        var temp = new DoublyLinkedListNode<U>(arr[ i ][ j ], i == 0 && j == 0);
        visited[ i ][ j ] = temp;

        temp.setEast(DoublyLinkedList.constructFromMatrix(arr, i, j + 1, m, n, visited))
            .ifPresent(east -> east.setWest(temp));

        temp.setSouth(DoublyLinkedList.constructFromMatrix(arr, i + 1, j, m, n, visited))
            .ifPresent(south -> south.setNorth(temp));

        temp.setWest(DoublyLinkedList.constructFromMatrix(arr, i, j - 1, m, n, visited))
            .ifPresent(west -> west.setEast(temp));

        temp.setNorth(DoublyLinkedList.constructFromMatrix(arr, i - 1, j, m, n, visited))
            .ifPresent(north -> north.setSouth(temp));

        return temp;
    }

    public static <T> DoublyLinkedList<T> from(T[][] matrix) {
        if (matrix.length == 0) {
            throw new IllegalArgumentException("Matrix must not be empty.");
        }

        int m = matrix.length;
        int n = matrix[ 0 ].length;

        DoublyLinkedListNode<T>[][] visited = new DoublyLinkedListNode[ m ][ n ];

        var anchor = DoublyLinkedList.constructFromMatrix(matrix, 0, 0, m, n, visited);

        return new DoublyLinkedList<>(anchor);
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

    public DoublyLinkedListNode<T> getAnchor() {
        return this.anchor;
    }
}
