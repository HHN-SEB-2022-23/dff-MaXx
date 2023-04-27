package de.hhn.view;

import java.awt.*;
import javax.swing.*;

/** simple dialog for displaying a modal message box */
public class MessageDialog extends JDialog {

  public MessageDialog(final String title, String message) {
    // if message contains newlines, split it up inti html paragraphs
    if (message.contains("\n")) {
      message = message.replace("\n", "</p><p>");
      message = "<html><p>" + message + "</p></html>";
    }
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setMinimumSize(new Dimension(300, 150));
    this.setBackground(Theme.background);
    this.setForeground(Theme.foreground);
    this.setModal(true);
    this.setResizable(false);
    this.setTitle(title);

    final var panel = new JPanel();
    panel.setBackground(Theme.background);
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    final var textEl = new JLabel(message);
    textEl.setForeground(Theme.foreground);
    textEl.setOpaque(false);
    panel.add(textEl);

    final var button = new SimpleButton("OK", e -> this.dispose());
    panel.add(button, BorderLayout.SOUTH);

    this.add(panel);
    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }

  public static void show(final String title, final String message) {
    new MessageDialog(title, message);
  }
}