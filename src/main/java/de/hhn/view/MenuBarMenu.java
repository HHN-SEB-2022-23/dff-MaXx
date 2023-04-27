package de.hhn.view;

import java.awt.*;
import javax.swing.*;

/** menu entry for the menu bar */
public class MenuBarMenu extends JMenu {

  public MenuBarMenu(final String s) {
    super(s);
    this.setFont(Theme.font);
    this.setForeground(Theme.foreground);
    this.setBackground(Theme.background);
    this.setOpaque(true);
  }

  @Override
  public void paintComponent(final Graphics g) {
    final Graphics2D g2d = (Graphics2D) g;
    final var fm = g2d.getFontMetrics();
    final var txt = this.getText();
    final var width = this.getWidth();
    final var height = this.getHeight();
    final var txtWidth = fm.stringWidth(txt);
    g2d.setColor(Theme.background);
    g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
    g2d.setColor(Theme.foreground);
    g2d.drawString(txt, (width - txtWidth) / 2, height - 5);
  }
}