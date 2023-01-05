package de.hhn;

import de.hhn.lib.DoublyLinkedList;
import de.hhn.lib.Vector2D;

import java.util.Optional;
import java.util.Scanner;

/**
 * Oberfläche für das Spiel.
 * <p>
 * Ausgabe und Eingabe über die Konsole.
 */
public class GameScreen {
    protected static final int fieldSize = 8;
    protected static final String fieldFormat = "%-" + GameScreen.fieldSize + 's';
    protected final Scanner scanner;
    protected CharacterKind currentPlayer;
    protected ReadOnlyCharacter characterB;
    protected ReadOnlyCharacter characterW;

    public GameScreen() {
        this.scanner = new Scanner(System.in);
    }

    private static void printHead(char c, StringBuilder sb) {
        sb.append(" ┿");
        var space = "━━".repeat(GameScreen.fieldSize << 1);
        sb.append(space);
        sb.append(c);
        sb.append(space);
        sb.append("┿\n");
    }

    private static void printField(Object field, StringBuilder sb) {
        var fieldStr = field.toString();
        sb.append(String.format(
            GameScreen.fieldFormat,
            " ".repeat(( GameScreen.fieldSize >> 1 ) - ( fieldStr.length() >> 1 ) - 1) + fieldStr
        ));
    }

    private static void printPlayerPoints(ReadOnlyCharacter character, StringBuilder sb) {
        sb.append(String.format(
            "Player W has %s points (~ %d)%n",
            character.getPoints(),
            character.getPoints().intValue()
        ));
    }

    public static void drawWinner(ReadOnlyCharacter winner) {
        GameScreen.clearScreen();
        System.out.printf("Player %s has won with ~%s points!%n", winner, winner.getPoints().floatValue());
    }

    protected static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void draw(
        DoublyLinkedList<? extends ReadOnlyField> fields,
        ReadOnlyCharacter characterB,
        ReadOnlyCharacter characterW,
        CharacterKind nextMovingCharacter
    ) {
        this.currentPlayer = nextMovingCharacter;
        this.characterB = characterB;
        this.characterW = characterW;

        var rowStart = fields.getAnchor();
        var currentNode = rowStart;

        var sb = new StringBuilder();
        GameScreen.printHead('N', sb);
        sb.append(" │ ");
        var y = 0;
        var x = 0;
        while (true) {
            // Character B here?
            if (x == characterB.getPosition().x() && y == characterB.getPosition().y()) {
                GameScreen.printField(characterB, sb);
            }

            // Character W here?
            else if (x == characterW.getPosition().x() && y == characterW.getPosition().y()) {
                GameScreen.printField(characterW, sb);
            }

            // Field
            else {
                GameScreen.printField(currentNode.getData(), sb);
            }

            var east = currentNode.getEast();
            if (east.isPresent()) {
                currentNode = east.get();
                x++;
            }
            else {
                var south = rowStart.getSouth();

                if (south.isPresent()) {
                    y++;
                    x = 0;
                    if (y == 4) {
                        sb.append("│E\nW│ ");
                    }
                    else {
                        sb.append("│ \n │ ");
                    }

                    currentNode = rowStart = south.get();
                }
                else {
                    sb.append("│\n");
                    break;
                }
            }
        }

        GameScreen.printHead('S', sb);
        GameScreen.printPlayerPoints(this.characterB, sb);
        GameScreen.printPlayerPoints(this.characterW, sb);
        GameScreen.clearScreen();
        System.out.println(sb);
    }

    public Optional<Move> getNextMove() {
        while (true) {
            System.out.printf("%s Richtung: ", this.currentPlayer);
            String command = this.scanner.nextLine().toLowerCase();

            switch (command) {
                case "n" -> {
                    return Optional.of(
                        new Move(this.currentPlayer, new Vector2D(0, -1))
                    );
                }
                case "s" -> {
                    return Optional.of(
                        new Move(this.currentPlayer, new Vector2D(0, 1))
                    );
                }
                case "w" -> {
                    return Optional.of(
                        new Move(this.currentPlayer, new Vector2D(-1, 0))
                    );
                }
                case "e" -> {
                    return Optional.of(
                        new Move(this.currentPlayer, new Vector2D(1, 0))
                    );
                }
                case "q", "quit", "exit", "stop" -> {
                    return Optional.empty();
                }
                case "no" -> {
                    if (this.currentPlayer == CharacterKind.WHITE) {
                        return Optional.of(
                            new Move(this.currentPlayer, new Vector2D(1, -1))
                        );
                    }
                }
                case "sw" -> {
                    if (this.currentPlayer == CharacterKind.BLACK) {
                        return Optional.of(
                            new Move(this.currentPlayer, new Vector2D(-1, 1))
                        );
                    }
                }
            }

            System.out.println("Ungültige Eingabe!");
        }
    }
}
