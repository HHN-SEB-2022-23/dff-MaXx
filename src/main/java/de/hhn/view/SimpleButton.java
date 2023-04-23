package de.hhn.view;

import java.awt.event.ActionListener;
import javax.swing.*;

class SimpleButton extends JButton {

  public SimpleButton(final String text, final ActionListener onCLick) {
    this(text);
    this.addActionListener(onCLick);
  }

  public SimpleButton(final String text) {
    super(text);
    this.setFont(Theme.font);
    this.setOpaque(false);
    this.setBackground(Theme.background);
    this.setForeground(Theme.accent);
    this.setRolloverEnabled(false);
    this.setFocusable(false);
    this.setBorderPainted(false);
  }
}