package de.hhn.lib;

import java.util.Optional;

/**
 * Ein Feld in der DoublyLinkedList
 */
public class DoublyLinkedListNode<T> {
    protected T data;
    protected DoublyLinkedListNode<T> north;
    protected DoublyLinkedListNode<T> south;
    protected DoublyLinkedListNode<T> east;
    protected DoublyLinkedListNode<T> west;
    public final boolean isAnchor;

    public DoublyLinkedListNode(T data, boolean isAnchor) {
        this.data = data;
        this.isAnchor = isAnchor;
    }

    public T getData() {
        return this.data;
    }

    public Optional<DoublyLinkedListNode<T>> getNorth() {
        return Optional.ofNullable(this.north);
    }

    public Optional<DoublyLinkedListNode<T>> setNorth(DoublyLinkedListNode<T> north) {
        return Optional.ofNullable(this.north = north);
    }

    public Optional<DoublyLinkedListNode<T>> getSouth() {
        return Optional.ofNullable(this.south);
    }

    public Optional<DoublyLinkedListNode<T>> setSouth(DoublyLinkedListNode<T> south) {
        return Optional.ofNullable(this.south = south);
    }

    public Optional<DoublyLinkedListNode<T>> getEast() {
        return Optional.ofNullable(this.east);
    }

    public Optional<DoublyLinkedListNode<T>> setEast(DoublyLinkedListNode<T> east) {
        return Optional.ofNullable(this.east = east);
    }

    public Optional<DoublyLinkedListNode<T>> getWest() {
        return Optional.ofNullable(this.west);
    }

    public Optional<DoublyLinkedListNode<T>> setWest(DoublyLinkedListNode<T> west) {
        return Optional.ofNullable(this.west = west);
    }
}
