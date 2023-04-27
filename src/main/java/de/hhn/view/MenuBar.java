package de.hhn.view;

import java.awt.*;
import javax.swing.*;

/** menu bar for a game instance */
public class MenuBar extends JMenuBar {
  public MenuBar(final ActionListeners al) {
    final var saveGameMenu = new MenuBarMenu("Game");
    final var loadSaveGame = new JMenuItem("Load");
    loadSaveGame.addActionListener(al::loadGame);
    saveGameMenu.add(loadSaveGame);
    this.add(saveGameMenu);
    final var saveSaveGame = new JMenuItem("Save");
    saveSaveGame.addActionListener(al::saveGame);
    saveGameMenu.add(saveSaveGame);
    final var aboutMenu = new MenuBarMenu("About");
    final var ourTeam = new JMenuItem("Our team");
    ourTeam.addActionListener(al::ourTeam);
    aboutMenu.add(ourTeam);
    final var manualBtn = new JMenuItem("Manual");
    manualBtn.addActionListener(al::manual);
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