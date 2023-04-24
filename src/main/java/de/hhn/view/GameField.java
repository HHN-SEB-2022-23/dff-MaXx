package de.hhn.view;

import de.hhn.lib.Vector2D;
import de.hhn.model.Fraction;
import java.awt.*;
import javax.swing.*;

public class GameField extends JButton {

  private final Vector2D position;
  private Fraction fraction = null;

  public GameField(final Vector2D position) {
    super("N/A");
    this.position = position;
  }

  public Vector2D getPosition() {
    return this.position;
  }

  @Override
  public void paintComponent(final Graphics g) {
    g.setColor(this.getBackground());
    g.fillRect(0, 0, this.getWidth(), this.getHeight());
    g.setColor(this.getForeground());

    final var fm = g.getFontMetrics();
    if (this.fraction != null) {
      if (this.fraction.equals(Fraction.ZERO)) {
        return;
      }

      GameScreen.renderFraction(g, fm, this, this.fraction);
    } else {
      final var textWidth = fm.stringWidth(this.getText());
      final var textHeight = fm.getHeight();
      g.drawString(
          this.getText(), (this.getWidth() - textWidth) / 2, (this.getHeight() + textHeight) / 2);
    }
  }

  public void setFraction(final Fraction value) {
    this.fraction = value;
    super.setText(value.toString());
  }

  @Override
  public void setText(final String text) {
    this.fraction = null;
    super.setText(text);
  }
}