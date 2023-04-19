package de.hhn;

import de.hhn.lib.DoublyLinkedList;
import de.hhn.lib.Vector2D;
import de.hhn.services.RuleChecker;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Oberfläche für das Spiel.
 * <p>
 * Ausgabe und Eingabe über die Konsole.
 */
public class GameScreen extends JFrame {
    protected static final Color deepPink = new Color(255, 20, 147);
    protected static final Color veryDarkGray = new Color(37, 35, 38);
    protected static final Color fairlyLightGray = new Color(115, 100, 110);
    protected static final Color whiteSmoke = new Color(245, 245, 245);
    protected static final Font font = new Font("Arial", Font.PLAIN, 18);
    protected static Border defaultBorder = BorderFactory.createLineBorder(GameScreen.fairlyLightGray, 1);
    protected static Border highlightBorder = BorderFactory.createLineBorder(GameScreen.deepPink, 2);
    protected final Map<Vector2D, GameField> fields = new HashMap<>(64);
    private final FractionLabel statusBlackEl;
    private final FractionLabel statusWhiteEl;
    private ReadOnlyCharacter currentChar;

    public GameScreen(Controller controller) {
        super("MaXx");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(GameScreen.veryDarkGray);
        var mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(GameScreen.veryDarkGray);

        var gridPanel = new JPanel(new GridLayout(8, 8));
        gridPanel.setBackground(GameScreen.veryDarkGray);
        var fieldElSize = new Dimension(128, 64);
        for (var y = 0; y < 8; ++y) {
            for (var x = 0; x < 8; ++x) {
                var fieldPos = new Vector2D(x, y);
                var fieldEl = new GameField(fieldPos);
                fieldEl.setFont(GameScreen.font);
                fieldEl.setForeground(GameScreen.whiteSmoke);
                fieldEl.setOpaque(false);
                fieldEl.setPreferredSize(fieldElSize);
                fieldEl.addActionListener(controller);
                gridPanel.add(fieldEl);
                this.fields.put(fieldPos, fieldEl);
            }
        }
        mainPanel.add(gridPanel);

        var statusPanel = new JPanel();
        statusPanel.setOpaque(false);
        statusPanel.setLayout(new GridLayout(2, 3));
        mainPanel.add(statusPanel);

        statusPanel.add(new SimpleButton("Manual", e -> {
            MessageDialog.show("Manual",
                "<html>" +
                "<p>Bewege die Figuren per klick auf ein begehbares Feld.</p>" +
                "<p>Begehbare Felder sind mit einer Umrandung gekennzeichnet.</p>" +
                "<p>Beide Figuren können sich in die vier Himmeldrichtungen bewegen.</p>" +
                "<p>Die weiße Figur kann sich auch nach Nord Osten bewegen,</p>" +
                "<p>die schwarze Figur nach Nord Westen.</p>" +
                "<p>Schwarz beginnt.</p>" +
                "<p>Wenn ein Spieler das Feld eines anderen Spielers betritt, wird dieser geschlagen,</p>" +
                "<p>der geschlagene Spieler gibt seine Punkte an seinen Gegner ab und wird auf das Startfeld gesetzt.</p>" +
                "</html>"
            );
        }));

        var statusBlackLabel = new JLabel("Black:");
        statusBlackLabel.setFont(GameScreen.font);
        statusBlackLabel.setForeground(GameScreen.whiteSmoke);
        statusBlackLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusBlackLabel.setVerticalAlignment(SwingConstants.CENTER);
        statusPanel.add(statusBlackLabel);
        this.statusBlackEl = new FractionLabel();
        this.statusBlackEl.setFont(GameScreen.font);
        this.statusBlackEl.setForeground(GameScreen.whiteSmoke);
        this.statusBlackEl.setBackground(GameScreen.veryDarkGray);
        this.statusBlackEl.setPreferredSize(fieldElSize);
        statusPanel.add(this.statusBlackEl);

        statusPanel.add(new SimpleButton("Credits", e -> {
            var contributors = Arrays.asList("Frank Mayer", "René Ott", "Antonia Friese", "Felix Marzioch");
            Collections.shuffle(contributors);
            MessageDialog.show("Credits", String.join(", ", contributors));
        }));

        var statusWhiteLabel = new JLabel("White:");
        statusWhiteLabel.setFont(GameScreen.font);
        statusWhiteLabel.setForeground(GameScreen.whiteSmoke);
        statusWhiteLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusWhiteLabel.setVerticalAlignment(SwingConstants.CENTER);
        statusPanel.add(statusWhiteLabel);
        this.statusWhiteEl = new FractionLabel();
        this.statusWhiteEl.setFont(GameScreen.font);
        this.statusWhiteEl.setForeground(GameScreen.whiteSmoke);
        this.statusWhiteEl.setBackground(GameScreen.veryDarkGray);
        this.statusWhiteEl.setPreferredSize(fieldElSize);
        statusPanel.add(this.statusWhiteEl);

        this.add(mainPanel);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void drawWinner(Character characterB) {
        MessageDialog.show("Finished", "Player %s is the winner".formatted(characterB));
    }

    private static void renderFraction(Graphics g, FontMetrics fm, JComponent component, Fraction fraction) {
        var numeratorText = fraction.getNumerator().toString();
        var numeratorWidth = fm.stringWidth(numeratorText);
        var denominatorText = fraction.getDenominator().toString();
        var denominatorWidth = fm.stringWidth(denominatorText);
        var textHeight = (double) fm.getHeight();
        var componentHeight = (double) component.getHeight();
        var componentWidth = (double) component.getWidth();
        var middle = componentHeight / 2.0;
        var paddingX = Math.ceil(component.getWidth() * 0.4);
        var paddingY = Math.ceil(component.getHeight() * 0.1);
        var magicValue = 2.0 / 3.0;

        // bruchstrich
        g.drawLine((int) paddingX, (int) middle, (int) ( componentWidth - paddingX ), (int) middle);

        // draw numerator
        g.drawString(numeratorText,
            (int) ( ( componentWidth - numeratorWidth ) / 2.0 ),
            (int) ( componentHeight / 2.0 - paddingY )
        );

        // draw denominator
        g.drawString(denominatorText,
            (int) ( ( componentWidth - denominatorWidth ) / 2.0 ),
            (int) ( componentHeight / 2.0 + textHeight * magicValue + paddingY )
        );
    }

    public void draw(DoublyLinkedList<Field> fields, Character characterB, Character characterW, CharacterKind characterKind) {
        this.currentChar = characterKind == CharacterKind.BLACK ? characterB : characterW;

        this.statusBlackEl.setFraction(characterB.getPoints());
        this.statusWhiteEl.setFraction(characterW.getPoints());

        var rowStartFld = fields.getAnchor();

        var currentlyPossibleMoves = RuleChecker.getAllMovesFor(characterKind)
            .stream()
            .map(delta -> this.currentChar.getPosition().add(delta))
            .toArray();

        // alle zeilen durchgehen
        while (rowStartFld != null) {
            // alle felder der zeile durchgehen
            var currentFld = rowStartFld;
            while (true) {
                // feld ausgeben
                var data = currentFld.getData();
                var fieldEl = this.fields.get(data.position);
                var backgroundColor = GameScreen.veryDarkGray;
                var border = GameScreen.defaultBorder;
                var textColor = GameScreen.whiteSmoke;
                if (data.position == characterB.getPosition()) {
                    fieldEl.setText(characterB.toString());
                    if (characterKind == CharacterKind.BLACK) {
                        backgroundColor = GameScreen.deepPink;
                        textColor = GameScreen.veryDarkGray;
                        border = GameScreen.highlightBorder;
                    }
                    else {
                        textColor = GameScreen.deepPink;
                    }
                }
                else if (data.position == characterW.getPosition()) {
                    fieldEl.setText(characterW.toString());
                    if (characterKind == CharacterKind.WHITE) {
                        backgroundColor = GameScreen.deepPink;
                        textColor = GameScreen.veryDarkGray;
                        border = GameScreen.highlightBorder;
                    }
                    else {
                        textColor = GameScreen.deepPink;
                    }
                }
                else {
                    fieldEl.setFraction(data.getValue());
                }

                if (Arrays.stream(currentlyPossibleMoves).anyMatch(data.position::equals)) {
                    border = GameScreen.highlightBorder;
                    fieldEl.setEnabled(true);
                }
                else {
                    fieldEl.setEnabled(false);
                }

                fieldEl.setBorder(border);
                fieldEl.setBackground(backgroundColor);
                fieldEl.setForeground(textColor);

                // nächstes feld
                var nextFldOpt = currentFld.getEast();
                if (nextFldOpt.isEmpty()) {
                    // nächste zeile
                    var nextRowStartFldOpt = rowStartFld.getSouth();
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

    public static class GameField extends JButton {
        private final Vector2D position;
        private boolean isFraction = false;
        private Fraction fraction = null;

        public GameField(Vector2D position) {
            super("N/A");
            this.position = position;
        }

        public Vector2D getPosition() {
            return this.position;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(this.getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(this.getForeground());

            var fm = g.getFontMetrics();
            if (this.isFraction) {
                if (this.fraction.equals(Fraction.ZERO)) {
                    return;
                }

                GameScreen.renderFraction(g, fm, this, this.fraction);
            }
            else {
                var textWidth = fm.stringWidth(this.getText());
                var textHeight = fm.getHeight();
                g.drawString(this.getText(),
                    ( this.getWidth() - textWidth ) / 2,
                    ( this.getHeight() + textHeight ) / 2
                );
            }
        }

        public void setFraction(Fraction value) {
            this.isFraction = true;
            this.fraction = value;
        }

        @Override
        public void setText(String text) {
            this.isFraction = false;
            super.setText(text);
        }
    }

    private static class FractionLabel extends JLabel {
        private Fraction fraction;

        public FractionLabel() {
            this.fraction = Fraction.ZERO;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(this.getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(this.getForeground());

            var fm = g.getFontMetrics();
            GameScreen.renderFraction(g, fm, this, this.fraction);
        }

        public void setFraction(Fraction points) {
            this.fraction = points;
            this.repaint();
        }
    }

    private static class MessageDialog extends JDialog {
        public MessageDialog(String title, String message) {
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            this.setMinimumSize(new Dimension(300, 150));
            this.setBackground(GameScreen.veryDarkGray);
            this.setForeground(GameScreen.whiteSmoke);
            this.setModal(true);
            this.setResizable(false);
            this.setTitle(title);

            var panel = new JPanel();
            panel.setBackground(GameScreen.veryDarkGray);
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            var textEl = new JLabel(message);
            textEl.setForeground(GameScreen.whiteSmoke);
            textEl.setOpaque(false);
            panel.add(textEl);

            var button = new SimpleButton("OK", e -> this.dispose());
            panel.add(button, BorderLayout.SOUTH);

            this.add(panel);
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }

        public static void show(String title, String message) {
            new MessageDialog(title, message);
        }
    }

    private static class SimpleButton extends JButton {
        public SimpleButton(String text, ActionListener onCLick) {
            this(text);
            this.addActionListener(onCLick);
        }

        public SimpleButton(String text) {
            super(text);
            this.setFont(GameScreen.font);
            this.setOpaque(false);
            this.setBackground(GameScreen.veryDarkGray);
            this.setForeground(GameScreen.deepPink);
            this.setRolloverEnabled(false);
            this.setFocusable(false);
            this.setBorderPainted(false);
        }
    }
}
