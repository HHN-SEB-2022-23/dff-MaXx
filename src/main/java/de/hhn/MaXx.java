package de.hhn;

import de.hhn.view.MainMenu;
import javax.swing.*;

/** Startet den controller. */
public class MaXx {
  public static void main(String[] args) {
    try {
      System.setProperty("apple.laf.useScreenMenuBar", "true");
      System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MaXx");
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException
        | InstantiationException
        | IllegalAccessException
        | UnsupportedLookAndFeelException ignore) {
      // dann halt nicht 😒
    }

    new MainMenu();
  }
}