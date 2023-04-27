package de.hhn.view;

import de.hhn.controller.Controller;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * main game menu
 * starts the game instances
 */
public class MainMenu extends JFrame {

  private final List<Controller> controllers = new LinkedList<>();

  public MainMenu() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setBackground(Theme.background);
    this.setResizable(false);

    final var panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(Theme.background);
    panel.setBorder(BorderFactory.createEmptyBorder(50, 75, 50, 75));
    this.add(panel);

    final var title = new JLabel("MaXx");
    title.setFont(new Font("Arial", Font.BOLD, 48));
    title.setForeground(Theme.foreground);
    panel.add(title);

    final var newGameButton = new SimpleButton("New Game", this::onNewGameButtonClicked);
    panel.add(newGameButton);

    final var loadGameButton = new SimpleButton("Load Game", this::onLoadGameButtonClicked);
    panel.add(loadGameButton);

    final var quitButton = new SimpleButton("Quit", this::onQuitButtonClicked);
    panel.add(quitButton);

    this.pack();
    this.setVisible(true);
  }

  private void onLoadGameButtonClicked(final ActionEvent e) {
    final var controller = new Controller(true);
    controller.start();
    controllers.add(controller);
  }

  private void onNewGameButtonClicked(final ActionEvent e) {
    final var controller = new Controller(false);
    controller.start();
    controllers.add(controller);
  }

  private void onQuitButtonClicked(final ActionEvent e) {
    for (final var controller : this.controllers) {
      controller.dispose();
    }
    this.dispose();
  }
}