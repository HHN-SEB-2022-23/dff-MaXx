package de.hhn;

import de.hhn.lib.DoublyLinkedListNode;
import de.hhn.lib.Vector2D;

/**
 * Hält Daten, die den einzelnen Spieler betreffen.
 */
public class Character implements ReadOnlyCharacter {
    private final CharacterKind kind;
    private DoublyLinkedListNode<Field> fieldNode;
    private Fraction points;

    public Character(CharacterKind kind, DoublyLinkedListNode<Field> fieldNode) {
        this.points = Fraction.ZERO;
        this.kind = kind;
        this.fieldNode = fieldNode;

        this.fieldNode.getData().setZero();
    }

    public void incrementPoints(Fraction points) {
        this.points = this.points.add(points);
    }

    @Override
    public Fraction getPoints() {
        return this.points;
    }

    public void resetPoints() {
        this.points = Fraction.ZERO;
    }

    @Override
    public Vector2D getPosition() {
        return this.fieldNode.getData().position;
    }

    public void teleport(DoublyLinkedListNode<Field> targetField) {
        this.fieldNode = targetField;
    }

    @Override
    public String toString() {
        return this.kind.toString();
    }

    @Override
    public CharacterKind getKind() {
        return this.kind;
    }

    public void move(Vector2D direction) {
        if (direction.x() == 1) {
            this.fieldNode.getEast()
                .ifPresent(eastNode -> this.fieldNode = eastNode);
        }
        else if (direction.x() == -1) {
            this.fieldNode.getWest()
                .ifPresent(westNode -> this.fieldNode = westNode);
        }

        if (direction.y() == 1) {
            this.fieldNode.getSouth()
                .ifPresent(southNode -> this.fieldNode = southNode);
        }
        else if (direction.y() == -1) {
            this.fieldNode.getNorth()
                .ifPresent(northNode -> this.fieldNode = northNode);
        }

        this.incrementPoints(this.fieldNode.getData().getValue());
        this.fieldNode.getData().setZero();
    }
}
