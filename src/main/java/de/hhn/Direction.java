package de.hhn;

public enum Direction {
    NORTH("N"),
    SOUTH("S"),
    EAST("E"),
    WEST("W"),
    NORTH_EAST("NE"),
    SOUTH_WEST("SW");

    private final String displayName;

    Direction(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }

    public Direction getOpposite() {
        return switch (this) {
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case EAST -> Direction.WEST;
            case WEST -> Direction.EAST;
            case NORTH_EAST -> Direction.SOUTH_WEST;
            case SOUTH_WEST -> Direction.NORTH_EAST;
        };
    }
}
