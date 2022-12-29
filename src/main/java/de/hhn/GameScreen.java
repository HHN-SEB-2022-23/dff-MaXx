package de.hhn;

import de.hhn.lib.DoublyLinkedList;

import java.util.Optional;
import java.util.Scanner;

public class GameScreen {
    protected final Scanner scanner;
    protected CharacterKind currentPlayer;
    protected ReadOnlyCharacter characterB;
    protected ReadOnlyCharacter characterW;
    protected static final int fieldSize = 8;
    protected static final String fieldFormat = "%-" + GameScreen.fieldSize + 's';

    public GameScreen() {
        this.scanner = new Scanner(System.in);
    }

    private static void printHead(char c) {
        var sb = new StringBuilder(2 + ( GameScreen.fieldSize << 2 ));
        sb.append(' ');

        var emptyField = " ".repeat(GameScreen.fieldSize);
        for (int i = 0; i < 4; i++) {
            sb.append(emptyField);
        }

        sb.append(c);

        System.out.println(sb);
    }

    private static void printField(ReadOnlyField field) {
        System.out.printf(GameScreen.fieldFormat, field);
    }

    public void draw(
        DoublyLinkedList<? extends ReadOnlyField> fields,
        ReadOnlyCharacter characterB,
        ReadOnlyCharacter characterW,
        CharacterKind nextMovingCharacter
    ) {
        GameScreen.clearScreen();
        this.currentPlayer = nextMovingCharacter;
        this.characterB = characterB;
        this.characterW = characterW;

        var rowStart = fields.getAnchor();
        var currentNode = rowStart;
        var y = 0;

        GameScreen.printHead('N');
        System.out.print("  ");
        while (true) {
            GameScreen.printField(currentNode.getData());

            var east = currentNode.getEast();
            if (east.isPresent()) {
                currentNode = east.get();
            } else {
                var south = rowStart.getSouth();

                if (south.isPresent()) {
                    y++;
                    if (y == 4) {
                        System.out.print(" E");
                        System.out.println();
                        System.out.print("W ");
                    }
                    else {
                        System.out.print("  ");
                        System.out.println();
                        System.out.print("  ");
                    }

                    currentNode = rowStart = south.get();
                } else {
                    break;
                }
            }
        }

        System.out.println();
        GameScreen.printHead('S');
        System.out.println();
        System.out.println("Next moving character: " + nextMovingCharacter);
    }

    public Optional<Move> getNextMove() {
        while(true) {
            System.out.print("Richtung: ");
            String command = this.scanner.nextLine().toLowerCase();

            switch (command) {
                case "n", "s", "e", "w" -> {
                    return Optional.of(new Move());
                }
                case "q", "quit", "exit", "stop" -> {
                    return Optional.empty();
                }
                case "no" -> {
                    if (this.currentPlayer == CharacterKind.WHITE) {
                        return Optional.of(new Move());
                    }
                }
                case "sw" -> {
                    if (this.currentPlayer == CharacterKind.BLACK) {
                        return Optional.of(new Move());
                    }
                }
            }

            System.out.println("Ungültige Eingabe!");
        }
    }

    protected static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
