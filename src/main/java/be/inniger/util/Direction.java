package be.inniger.util;

public enum Direction {
    //@formatter:off
    NORTH,
    EAST,
    SOUTH,
    WEST,
    ;
    //@formatter:on

    public Direction turnLeft() {
        return switch (this) {
            case NORTH -> Direction.WEST;
            case EAST -> Direction.NORTH;
            case SOUTH -> Direction.EAST;
            case WEST -> Direction.SOUTH;
        };
    }

    public Direction turnRight() {
        return switch (this) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
        };
    }
}
