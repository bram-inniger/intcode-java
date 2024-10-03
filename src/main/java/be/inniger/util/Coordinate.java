package be.inniger.util;

public record Coordinate(long x, long y) {

    public Coordinate move(Direction direction) {
        return switch (direction) {
            case NORTH -> new Coordinate(this.x, this.y - 1);
            case EAST -> new Coordinate(this.x + 1, this.y);
            case SOUTH -> new Coordinate(this.x, this.y + 1);
            case WEST -> new Coordinate(this.x - 1, this.y);
        };
    }
}
