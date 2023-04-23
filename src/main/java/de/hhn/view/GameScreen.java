package de.hhn.view;

import de.hhn.lib.DoublyLinkedList;
import de.hhn.lib.Vector2D;
import de.hhn.model.*;
import de.hhn.model.Character;
import de.hhn.services.RuleChecker;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 * Oberfläche für das Spiel.
 *
 * <p>
 * Ausgabe und Eingabe über die Konsole.
 */
public class GameScreen extends JFrame {

  protected final Map<Vector2D, GameField> fields = new HashMap<>(64);
  private final FractionLabel statusBlackEl;
  private final FractionLabel statusWhiteEl;
  private ReadOnlyCharacter currentChar;

  public GameScreen(final ActionListener actionListener, final WindowAdapter windowAdapter) {
    super("MaXx");

    // menu bar
    this.setJMenuBar(new MenuBar());

    // Frame setup
    this.addWindowListener(windowAdapter);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setBackground(Theme.background);
    final var mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBackground(Theme.background);

    final var gridPanel = new JPanel(new GridLayout(8, 8));
    gridPanel.setBackground(Theme.background);
    final var fieldElSize = new Dimension(128, 64);
    for (var y = 0; y < 8; ++y) {
      for (var x = 0; x < 8; ++x) {
        final var fieldPos = new Vector2D(x, y);
        final var fieldEl = new GameField(fieldPos);
        fieldEl.setFont(Theme.font);
        fieldEl.setForeground(Theme.foreground);
        fieldEl.setOpaque(false);
        fieldEl.setPreferredSize(fieldElSize);
        fieldEl.addActionListener(actionListener);
        gridPanel.add(fieldEl);
        this.fields.put(fieldPos, fieldEl);
      }
    }
    mainPanel.add(gridPanel);

    final var statusPanel = new JPanel();
    statusPanel.setOpaque(false);
    statusPanel.setLayout(new GridLayout(2, 2));
    mainPanel.add(statusPanel);

    final var statusBlackLabel = new JLabel("Black:");
    statusBlackLabel.setFont(Theme.font);
    statusBlackLabel.setForeground(Theme.foreground);
    statusBlackLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    statusBlackLabel.setVerticalAlignment(SwingConstants.CENTER);
    statusPanel.add(statusBlackLabel);
    this.statusBlackEl = new FractionLabel();
    this.statusBlackEl.setFont(Theme.font);
    this.statusBlackEl.setForeground(Theme.foreground);
    this.statusBlackEl.setBackground(Theme.background);
    this.statusBlackEl.setPreferredSize(fieldElSize);
    statusPanel.add(this.statusBlackEl);

    final var statusWhiteLabel = new JLabel("White:");
    statusWhiteLabel.setFont(Theme.font);
    statusWhiteLabel.setForeground(Theme.foreground);
    statusWhiteLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    statusWhiteLabel.setVerticalAlignment(SwingConstants.CENTER);
    statusPanel.add(statusWhiteLabel);
    this.statusWhiteEl = new FractionLabel();
    this.statusWhiteEl.setFont(Theme.font);
    this.statusWhiteEl.setForeground(Theme.foreground);
    this.statusWhiteEl.setBackground(Theme.background);
    this.statusWhiteEl.setPreferredSize(fieldElSize);
    statusPanel.add(this.statusWhiteEl);

    this.add(mainPanel);

    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }

  public static void drawWinner(final Character characterB) {
    MessageDialog.show("Finished", "Player %s is the winner".formatted(characterB));
  }

  public static void renderFraction(
      final Graphics g, final FontMetrics fm, final JComponent component, final Fraction fraction) {
    final var numeratorText = fraction.getNumerator().toString();
    final var numeratorWidth = fm.stringWidth(numeratorText);
    final var denominatorText = fraction.getDenominator().toString();
    final var denominatorWidth = fm.stringWidth(denominatorText);
    final var textHeight = (double) fm.getHeight();
    final var componentHeight = (double) component.getHeight();
    final var componentWidth = (double) component.getWidth();
    final var middle = componentHeight / 2.0;
    final var paddingX = Math.ceil(component.getWidth() * 0.4);
    final var paddingY = Math.ceil(component.getHeight() * 0.1);
    final var magicValue = 2.0 / 3.0;

    // bruchstrich
    g.drawLine((int) paddingX, (int) middle, (int) (componentWidth - paddingX), (int) middle);

    // draw numerator
    g.drawString(
        numeratorText,
        (int) ((componentWidth - numeratorWidth) / 2.0),
        (int) (componentHeight / 2.0 - paddingY));

    // draw denominator
    g.drawString(
        denominatorText,
        (int) ((componentWidth - denominatorWidth) / 2.0),
        (int) (componentHeight / 2.0 + textHeight * magicValue + paddingY));
  }

  public void draw(
      final DoublyLinkedList<? extends ReadOnlyField> fields,
      final Character characterB,
      final Character characterW,
      final CharacterKind characterKind) {
    if (characterKind == CharacterKind.BLACK) {
      this.currentChar = characterB;
    } else {
      this.currentChar = characterW;
    }

    this.statusBlackEl.setFraction(characterB.getPoints());
    this.statusWhiteEl.setFraction(characterW.getPoints());

    var rowStartFld = fields.getAnchor();

    final var currentlyPossibleMoves = RuleChecker.getAllMovesFor(characterKind).stream()
        .map(delta -> this.currentChar.getPosition().add(delta))
        .toArray();

    // alle zeilen durchgehen
    while (rowStartFld != null) {
      // alle felder der zeile durchgehen
      var currentFld = rowStartFld;
      while (true) {
        // feld ausgeben
        final var data = currentFld.getData();
        final var fieldEl = this.fields.get(data.getPosition());
        var backgroundColor = Theme.background;
        var border = Theme.secondaryBorder;
        var textColor = Theme.foreground;
        final var pos = data.getPosition();
        if (pos == characterB.getPosition()) {
          fieldEl.setText(characterB.toString());
          if (characterKind == CharacterKind.BLACK) {
            backgroundColor = Theme.accent;
            textColor = Theme.background;
            border = Theme.accentBorder;
          } else {
            textColor = Theme.accent;
          }
        } else if (pos == characterW.getPosition()) {
          fieldEl.setText(characterW.toString());
          if (characterKind == CharacterKind.WHITE) {
            backgroundColor = Theme.accent;
            textColor = Theme.background;
            border = Theme.accentBorder;
          } else {
            textColor = Theme.accent;
          }
        } else {
          fieldEl.setFraction(data.getValue());
        }

        if (Arrays.asList(currentlyPossibleMoves).contains(pos)) {
          border = Theme.accentBorder;
          fieldEl.setEnabled(true);
        } else {
          fieldEl.setEnabled(false);
        }

        fieldEl.setBorder(border);
        fieldEl.setBackground(backgroundColor);
        fieldEl.setForeground(textColor);

        // nächstes feld
        final var nextFldOpt = currentFld.getEast();
        if (nextFldOpt.isEmpty()) {
          // nächste zeile
          final var nextRowStartFldOpt = rowStartFld.getSouth();
          if (nextRowStartFldOpt.isEmpty()) {
            return;
          }
          rowStartFld = nextRowStartFldOpt.get();
          break;
        }
        currentFld = nextFldOpt.get();
      }
    }
  }
}