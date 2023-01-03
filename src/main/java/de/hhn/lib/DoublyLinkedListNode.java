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

    public void setNorth(DoublyLinkedListNode<T> north) {
        this.north = north;
    }

    public Optional<DoublyLinkedListNode<T>> getSouth() {
        return Optional.ofNullable(this.south);
    }

    public void setSouth(DoublyLinkedListNode<T> south) {
        this.south = south;
    }

    public Optional<DoublyLinkedListNode<T>> getEast() {
        return Optional.ofNullable(this.east);
    }

    public void setEast(DoublyLinkedListNode<T> east) {
        this.east = east;
    }

    public Optional<DoublyLinkedListNode<T>> getWest() {
        return Optional.ofNullable(this.west);
    }

    public void setWest(DoublyLinkedListNode<T> west) {
        this.west = west;
    }
}
