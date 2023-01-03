package de.hhn;

import de.hhn.lib.DoublyLinkedListNode;
import de.hhn.lib.Vector2D;

/**
 * Hält Daten, die den einzelnen Spieler betreffen.
 */
public class Character implements ReadOnlyCharacter {
    private Fraction points;
    private CharacterKind kind;
    private Vector2D position;
    public DoublyLinkedListNode<Field> field;

    public Character(CharacterKind kind, DoublyLinkedListNode<Field> field, Vector2D position) {
        this.points = Fraction.ZERO;
        this.kind = kind;
        this.position = position;
        this.field = field;
    }

    public void incrementPoints(Fraction points) {
        this.points = this.points.add(points);
    }

    @Override
    public Fraction getPoints() {
        return this.points;
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        return this.kind.toString();
    }

    public void move(Vector2D direction) {
        this.position = this.position.add(direction);

        if (direction.getX() > 0) {
            this.field = this.field.getEast().get();
        }
        else {
            this.field = this.field.getWest().get();
        }

        if (direction.getY() > 0) {
            this.field = this.field.getSouth().get();
        }
        else {
            this.field = this.field.getNorth().get();
        }
    }
}
