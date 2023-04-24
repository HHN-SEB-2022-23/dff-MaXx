package de.hhn.model;

import de.hhn.lib.DoublyLinkedListNode;
import de.hhn.lib.Vector2D;

/** Hält Daten, die den einzelnen Spieler betreffen. */
public class Character implements ReadOnlyCharacter {
  private final CharacterKind kind;
  private DoublyLinkedListNode<? extends Field> fieldNode;
  private Fraction points;

  public Character(
      final CharacterKind kind, final DoublyLinkedListNode<? extends Field> fieldNode) {
    this.points = Fraction.ZERO;
    this.kind = kind;
    this.fieldNode = fieldNode;

    this.fieldNode.getData().setZero();
  }

  public void incrementPoints(final Fraction points) {
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

  public void setPosition(Vector2D pos) {
    this.fieldNode.getData().position = pos;
  }

  public void teleport(final DoublyLinkedListNode<? extends Field> targetField) {
    this.fieldNode = targetField;
  }

  public void teleport(final Vector2D targetPosition) {
    while(this.getPosition().x() < targetPosition.x()) {
      var eastNode = this.fieldNode.getEast();
      if (eastNode.isPresent()) {
        this.fieldNode = eastNode.get();
      } else {
        break;
      }
    }

    while(this.getPosition().x() > targetPosition.x()) {
      var westNode = this.fieldNode.getWest();
      if (westNode.isPresent()) {
        this.fieldNode = westNode.get();
      } else {
        break;
      }
    }

    while(this.getPosition().y() < targetPosition.y()) {
      var southNode = this.fieldNode.getSouth();
      if (southNode.isPresent()) {
        this.fieldNode = southNode.get();
      } else {
        break;
      }
    }

    while(this.getPosition().y() > targetPosition.y()) {
      var northNode = this.fieldNode.getNorth();
      if (northNode.isPresent()) {
        this.fieldNode = northNode.get();
      } else {
        break;
      }
    }
  }

  @Override
  public String toString() {
    return this.kind.toString();
  }

  @Override
  public CharacterKind getKind() {
    return this.kind;
  }

  public void move(final Vector2D direction) {
    if (direction.x() == 1) {
      this.fieldNode.getEast().ifPresent(eastNode -> this.fieldNode = eastNode);
    } else if (direction.x() == -1) {
      this.fieldNode.getWest().ifPresent(westNode -> this.fieldNode = westNode);
    }

    if (direction.y() == 1) {
      this.fieldNode.getSouth().ifPresent(southNode -> this.fieldNode = southNode);
    } else if (direction.y() == -1) {
      this.fieldNode.getNorth().ifPresent(northNode -> this.fieldNode = northNode);
    }

    this.incrementPoints(this.fieldNode.getData().getValue());
    this.fieldNode.getData().setZero();
  }

  public void setPoints(final Fraction points) {
    this.points = points;
  }
}