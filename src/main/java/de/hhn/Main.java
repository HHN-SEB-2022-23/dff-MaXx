package de.hhn;

import javax.swing.*;

/** Startet den Controller. */
public class Main {
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (ClassNotFoundException
        | InstantiationException
        | IllegalAccessException
        | UnsupportedLookAndFeelException ignore) {
      // dann halt nicht 😒
    }

    var controller = new Controller();
    controller.start();
  }
}