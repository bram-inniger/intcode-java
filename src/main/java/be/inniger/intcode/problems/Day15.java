package be.inniger.intcode.problems;

import be.inniger.intcode.IntCode;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day15 {

    private static final Coordinate ORIGIN = new Coordinate(0, 0);

    public static long partOne(List<Long> program) {
        return Area.map(new Droid(program)).findOxygen();
    }

    public static long partTwo(List<Long> program) {
        return Area.map(new Droid(program)).fillOxygen();
    }

    private enum Movement {
        //@formatter:off
        NORTH(1),
        SOUTH(2),
        WEST(3),
        EAST(4),
        ;
        //@formatter:on

        private final long code;

        Movement(long code) {
            this.code = code;
        }

        private Movement opposite() {
            return switch (this) {
                case NORTH -> SOUTH;
                case SOUTH -> NORTH;
                case WEST -> EAST;
                case EAST -> WEST;
            };
        }
    }

    private enum Status {
        //@formatter:off
        WALL(0),
        MOVED(1),
        DEST(2),
        ;
        //@formatter:on

        private final long code;

        Status(long code) {
            this.code = code;
        }

        private static Status of(long code) {
            return Arrays.stream(Status.values()).filter(status -> status.code == code).findFirst().orElseThrow();
        }
    }

    private enum Tile {
        //@formatter:off
        WALL("#"),
        EMPTY("."),
        OXYGEN_SYSTEM("O"),
        UNKNOWN(" "),
        ;
        //@formatter:on

        private final String display;

        Tile(String display) {
            this.display = display;
        }

        private static Tile of(Status status) {
            return switch (status) {
                case Status.WALL -> Tile.WALL;
                case Status.MOVED -> Tile.EMPTY;
                case Status.DEST -> Tile.OXYGEN_SYSTEM;
            };
        }
    }

    private static class Area {

        private final Droid droid;
        private final Map<Coordinate, Tile> tiles;

        private Area(Droid droid) {
            this.droid = droid;
            this.tiles = explore();
        }

        public static Area map(Droid droid) {
            return new Area(droid);
        }

        // BFS approach to find the shortest route
        public long findOxygen() {
            var destination = tiles.entrySet()
                    .stream()
                    .filter(e -> e.getValue() == Tile.OXYGEN_SYSTEM)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElseThrow();
            var visited = new HashSet<Coordinate>();
            var toVisit = new ArrayDeque<Node>();
            toVisit.addLast(new Node(ORIGIN, 0));

            while (!toVisit.isEmpty()) {
                var current = toVisit.removeFirst();

                // Found the destination, return
                if (current.coordinate.equals(destination)) {
                    return current.distance;
                }

                visited.add(current.coordinate);
                Arrays.stream(Movement.values())
                        .map(current.coordinate::move)
                        .filter(coordinate -> !visited.contains(coordinate))
                        .filter(coordinate -> {
                            var tile = tiles.getOrDefault(coordinate, Tile.UNKNOWN);
                            return tile == Tile.EMPTY || tile == Tile.OXYGEN_SYSTEM;
                        })
                        .forEach(coordinate -> toVisit.addLast(new Node(coordinate, current.distance + 1)));
            }

            throw new IllegalArgumentException("Destination cannot be reached from source");
        }

        // BFS approach to find the shortest route
        public long fillOxygen() {
            var source = tiles.entrySet()
                    .stream()
                    .filter(e -> e.getValue() == Tile.OXYGEN_SYSTEM)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElseThrow();
            var visited = new HashSet<Coordinate>();
            var toVisit = new ArrayDeque<Node>();
            toVisit.addLast(new Node(source, 0));

            while (!toVisit.isEmpty()) {
                var current = toVisit.removeFirst();

                visited.add(current.coordinate);
                Arrays.stream(Movement.values())
                        .map(current.coordinate::move)
                        .filter(coordinate -> !visited.contains(coordinate))
                        .filter(coordinate -> {
                            var tile = tiles.getOrDefault(coordinate, Tile.UNKNOWN);
                            return tile == Tile.EMPTY || tile == Tile.OXYGEN_SYSTEM;
                        })
                        .forEach(coordinate -> toVisit.addLast(new Node(coordinate, current.distance + 1)));

                if (toVisit.isEmpty()) {
                    return current.distance;
                }
            }

            throw new IllegalStateException("Unreachable");
        }

        // DFS approach to map the whole maze
        private Map<Coordinate, Tile> explore() {
            var tiles = new HashMap<Coordinate, Tile>();
            tiles.put(ORIGIN, Tile.EMPTY);
            Arrays.stream(Movement.values()).forEach(movement -> exploreHelper(movement, tiles));
            return tiles;
        }

        private void exploreHelper(Movement movement, Map<Coordinate, Tile> tiles) {
            var destination = droid.coordinate.move(movement);

            // This position has already been recorded, do nothing and return
            if (tiles.containsKey(destination)) {
                return;
            }

            // Initiate the movement to the destination, and record what is returned
            var tile = droid.move(movement);
            tiles.put(destination, tile);

            switch (tile) {
                case WALL -> {
                    // No actual movement happened, nothing needs to be restored, simply return
                }
                case EMPTY, OXYGEN_SYSTEM -> {
                    // Try all different moves from this new position
                    Arrays.stream(Movement.values()).forEach(nextMovement -> exploreHelper(nextMovement, tiles));

                    // Move back to the previous position
                    var oldTile = droid.move(movement.opposite());

                    // Verify this move succeeded
                    if (oldTile != Tile.EMPTY && oldTile != Tile.OXYGEN_SYSTEM) {
                        throw new IllegalStateException("Cannot move back to a previously valid position");
                    }
                }
                default -> throw new IllegalStateException("Invalid tile returned by the droid: " + tile);
            }
        }

        private Tile get(Coordinate coordinate) {
            return tiles.getOrDefault(coordinate, Tile.UNKNOWN);
        }

        @SuppressWarnings("unused")
        private String prettyPrint() {
            var minX = tiles.keySet()
                    .stream()
                    .filter(c -> tiles.get(c) != Tile.UNKNOWN)
                    .mapToLong(Coordinate::x)
                    .min()
                    .orElseThrow() - 2;
            var maxX = tiles.keySet()
                    .stream()
                    .filter(c -> tiles.get(c) != Tile.UNKNOWN)
                    .mapToLong(Coordinate::x)
                    .max()
                    .orElseThrow() + 2;
            var minY = tiles.keySet()
                    .stream()
                    .filter(c -> tiles.get(c) != Tile.UNKNOWN)
                    .mapToLong(Coordinate::y)
                    .min()
                    .orElseThrow() - 2;
            var maxY = tiles.keySet()
                    .stream()
                    .filter(c -> tiles.get(c) != Tile.UNKNOWN)
                    .mapToLong(Coordinate::y)
                    .max()
                    .orElseThrow() + 2;

            return LongStream.rangeClosed(minY, maxY)
                    .mapToObj(y -> LongStream.rangeClosed(minX, maxX)
                            .mapToObj(x -> new Coordinate(x, y))
                            .map(c -> c.equals(ORIGIN) ? "S" : get(c).display)
                            .collect(Collectors.joining()))
                    .collect(Collectors.joining("\n"));
        }

        private record Node(Coordinate coordinate, long distance) {
        }
    }

    private static class Droid {

        private final IntCode intCode;
        private Coordinate coordinate;

        private Droid(List<Long> program) {
            this.intCode = new IntCode(program);
            this.coordinate = ORIGIN;
        }

        private Tile move(Movement movement) {
            // Set input, run, parse outputs.
            // If move happened, update Coordinate
            intCode.input().add(movement.code);
            var status = intCode.run();

            if (status != IntCode.Status.INPUT_BLOCKED) {
                throw new IllegalStateException("The droid program should keep running");
            }

            var output = intCode.output().remove();
            var tile = Tile.of(Status.of(output));

            // Movement succeeded, update the internal position
            if (tile == Tile.EMPTY || tile == Tile.OXYGEN_SYSTEM) {
                coordinate = coordinate.move(movement);
            }

            return tile;
        }
    }

    private record Coordinate(long x, long y) {

        private Coordinate move(Movement movement) {
            return switch (movement) {
                case NORTH -> new Coordinate(this.x, this.y - 1);
                case SOUTH -> new Coordinate(this.x, this.y + 1);
                case WEST -> new Coordinate(this.x - 1, this.y);
                case EAST -> new Coordinate(this.x + 1, this.y);
            };
        }
    }
}
