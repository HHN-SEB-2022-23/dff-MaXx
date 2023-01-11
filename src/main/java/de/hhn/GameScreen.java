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

    private static void printHead(char c, StringBuilder sb, boolean no, boolean sw) {
        if (sw) {
            sb.append("SW");
        }
        else {
            sb.append("  ");
        }
        sb.append('┿');
        var space = "━━".repeat(GameScreen.fieldSize << 1);
        sb.append(space);
        sb.append(c);
        sb.append(space);
        sb.append('┿');
        if (no) {
            sb.append("NO");
        }
        sb.append('\n');
    }

    private static void printField(Object a, Object b, StringBuilder sb) {
        var fieldStr = String.format("%s&%s", a, b);
        sb.append(String.format(
            GameScreen.fieldFormat,
            " ".repeat(( GameScreen.fieldSize >> 1 ) - ( fieldStr.length() >> 1 ) - 1) + fieldStr
        ));
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
            "Player %s has %s points (~%d)%n",
            character.getKind(),
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
        GameScreen.printHead('N', sb, nextMovingCharacter == CharacterKind.WHITE, false);
        sb.append("  │ ");
        var y = 0;
        var x = 0;
        while (true) {
            // Character B & W here?
            if (x == characterB.getPosition().x() && x == characterW.getPosition().x()
                && y == characterB.getPosition().y() && y == characterW.getPosition().y()
            ) {
                GameScreen.printField(characterB, characterW, sb);
            }

            // Character B here?
            else if (x == characterB.getPosition().x() && y == characterB.getPosition().y()) {
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
                    if (y == 3) {
                        sb.append("│\n  W ");
                    }
                    else if (y == 4) {
                        sb.append("E\n  │ ");
                    }
                    else {
                        sb.append("│ \n  │ ");
                    }

                    currentNode = rowStart = south.get();
                }
                else {
                    sb.append("│\n");
                    break;
                }
            }
        }

        GameScreen.printHead('S', sb, false, nextMovingCharacter == CharacterKind.BLACK);
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
                    return Optional.of(
                        new Move(this.currentPlayer, new Vector2D(1, -1))
                    );
                }
                case "sw" -> {
                    return Optional.of(
                        new Move(this.currentPlayer, new Vector2D(-1, 1))
                    );
                }
            }

            System.out.println("Ungültige Eingabe! Gib eine Himmelsrichtung an, in die du dich bewegen möchtest.");
        }
    }

    public static void drawInvalidMove(CharacterKind kind) {
        System.out.printf("Player %s: Ungültiger Zug!%n", kind);

        var sb = new StringBuilder();

        sb.append("Erlaubte Bewegungen sind: N E S W ");

        if (kind == CharacterKind.BLACK) {
            sb.append("SW");
        }
        else {
            sb.append("NO");
        }

        System.out.println(sb);

        System.out.println("Die Spielfigur darf das Spielbrett nicht verlassen");
    }
}
