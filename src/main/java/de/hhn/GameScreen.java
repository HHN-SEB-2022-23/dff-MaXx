package de.hhn;

import de.hhn.lib.DoublyLinkedList;
import de.hhn.lib.Vector2D;
import de.hhn.services.RuleChecker;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Oberfläche für das Spiel.
 * <p>
 * Ausgabe und Eingabe über die Konsole.
 */
public class GameScreen extends JFrame implements ActionListener {
    protected static final Color deepPink = new Color(255, 20, 147);
    protected static final Color veryDarkGray = new Color(37, 35, 38);
    protected static final Color fairlyLightGray = new Color(115, 100, 110);
    protected static final Color whiteSmoke = new Color(245, 245, 245);
    protected static final Font font = new Font("Arial", Font.PLAIN, 18);
    protected final Map<Vector2D, GameField> fields = new HashMap<>();
    protected MoveCallback moveCallback;
    protected Border defaultBorder = BorderFactory.createLineBorder(GameScreen.fairlyLightGray, 1);
    protected Border selectableBorder = BorderFactory.createLineBorder(GameScreen.deepPink, 1);
    private ReadOnlyCharacter currentChar;

    public GameScreen() {
        super("MaXx");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 800);
        this.setBackground(GameScreen.veryDarkGray);
        var gridPanel = new JPanel(new GridLayout(8, 8));
        var fieldElSize = new Dimension(128, 64);
        for (var y = 0; y < 8; ++y) {
            for (var x = 0; x < 8; ++x) {
                var fieldPos = new Vector2D(x, y);
                var fieldEl = new GameField(fieldPos);
                fieldEl.setFont(GameScreen.font);
                fieldEl.setForeground(GameScreen.whiteSmoke);
                fieldEl.setOpaque(true);
                fieldEl.setPreferredSize(fieldElSize);
                fieldEl.setHorizontalAlignment(SwingConstants.CENTER);
                fieldEl.setVerticalAlignment(SwingConstants.CENTER);
                fieldEl.addActionListener(this);
                gridPanel.add(fieldEl);
                this.fields.put(fieldPos, fieldEl);
            }
        }
        this.add(gridPanel);
        this.pack();
        this.setVisible(true);
    }

    public static void drawWinner(Character characterB2) {
        showMessageDialog(null, "Winner is: " + characterB2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof GameField fieldEl) {
            if (!fieldEl.isEnabled()) {
                return;
            }

            var pos = fieldEl.getPosition();
            System.out.println("Clicked on: " + pos);
            this.moveCallback.onMove(new Move(this.currentChar.getKind(),
                pos.relativeTo(this.currentChar.getPosition())
            ));
        }
    }

    public void draw(DoublyLinkedList<Field> fields, Character characterB, Character characterW, CharacterKind characterKind) {
        this.currentChar = characterKind == CharacterKind.BLACK ? characterB : characterW;

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
                var border = this.defaultBorder;
                var textColor = GameScreen.whiteSmoke;
                if (data.position == characterB.getPosition()) {
                    fieldEl.setText("B");
                    if (characterKind == CharacterKind.BLACK) {
                        backgroundColor = GameScreen.deepPink;
                        textColor = GameScreen.veryDarkGray;
                    }
                    else {
                        textColor = GameScreen.deepPink;
                    }
                }
                else if (data.position == characterW.getPosition()) {
                    fieldEl.setText("W");
                    if (characterKind == CharacterKind.WHITE) {
                        backgroundColor = GameScreen.deepPink;
                        textColor = GameScreen.veryDarkGray;
                    }
                    else {
                        textColor = GameScreen.deepPink;
                    }
                }
                else {
                    fieldEl.setFraction(data.getValue());
                }

                if (Arrays.stream(currentlyPossibleMoves).anyMatch(data.position::equals)) {
                    border = this.selectableBorder;
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

    public void getNextMove(MoveCallback callback) {
        this.moveCallback = callback;
    }

    private static class GameField extends JButton {
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

        @Override // custom render
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
                
                var numeratorText = this.fraction.getNumerator().toString();
                var numeratorWidth = fm.stringWidth(numeratorText);
                var denominatorText = this.fraction.getDenominator().toString();
                var denominatorWidth = fm.stringWidth(denominatorText);
                var textHeight = (double) fm.getHeight();
                var componentHeight = (double) this.getHeight();
                var componentWidth = (double) this.getWidth();
                var middle = componentHeight / 2.0;
                var paddingX = Math.ceil(this.getWidth() * 0.4);
                var paddingY = Math.ceil(this.getHeight() * 0.1);
                var magicValue = 2.0 / 3.0;

                // bruchstrich
                g.drawLine((int) paddingX, (int) middle, (int) ( this.getWidth() - paddingX ), (int) middle);

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
}
