package de.hhn.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/** style of the UI */
public class Theme {

  public static final Color accent = new Color(255, 20, 147);
  public static final Color background = new Color(37, 35, 38);
  public static final Color secondary = new Color(115, 100, 110);
  public static final Color foreground = new Color(245, 245, 245);
  public static final Font font = new Font("Arial", Font.PLAIN, 18);
  public static final Border accentBorder = BorderFactory.createLineBorder(Theme.accent, 2);
  public static final Border secondaryBorder = BorderFactory.createLineBorder(Theme.secondary, 1);
}