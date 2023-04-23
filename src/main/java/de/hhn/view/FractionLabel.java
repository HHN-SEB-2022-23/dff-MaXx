package de.hhn.view;

import de.hhn.model.Fraction;
import java.awt.*;
import javax.swing.*;

class FractionLabel extends JLabel {

  private Fraction fraction;

  public FractionLabel() {
    this.fraction = Fraction.ZERO;
  }

  @Override
  public void paintComponent(final Graphics g) {
    g.setColor(this.getBackground());
    g.fillRect(0, 0, this.getWidth(), this.getHeight());
    g.setColor(this.getForeground());

    final var fm = g.getFontMetrics();
    GameScreen.renderFraction(g, fm, this, this.fraction);
  }

  public void setFraction(final Fraction points) {
    this.fraction = points;
    this.repaint();
  }
}