package de.hhn.view;

import java.awt.*;
import javax.swing.*;

public class MenuBar extends JMenuBar {
  public MenuBar() {
    final var saveGameMenu = new MenuBarMenu("Game");
    final var loadSaveGame = new JMenuItem("Load");
    saveGameMenu.add(loadSaveGame);
    this.add(saveGameMenu);
    final var aboutMenu = new MenuBarMenu("About");
    final var ourTeam = new JMenuItem("Our team");
    ourTeam.addActionListener(ActionListeners::ourTeam);
    aboutMenu.add(ourTeam);
    final var manualBtn = new JMenuItem("Manual");
    manualBtn.addActionListener(ActionListeners::manual);
    aboutMenu.add(manualBtn);
    this.add(aboutMenu);
  }

  @Override
  protected void paintComponent(final Graphics g) {
    final Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Theme.background);
    g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
  }
}