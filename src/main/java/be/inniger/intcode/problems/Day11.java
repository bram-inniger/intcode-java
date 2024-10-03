package be.inniger.intcode.problems;

import be.inniger.intcode.IntCode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day11 {

    public static long partOne(List<Long> program) {
        var hull = new Hull();
        hull.run(program);

        return hull.visited.size();
    }

    public static String partTwo(List<Long> program) {
        var hull = new Hull();
        hull.whitePanels.add(new Coordinate(0, 0));
        hull.run(program);

        return prettyPrint(hull.whitePanels);
    }

    private static String prettyPrint(Set<Coordinate> whitePanels) {
        var minX = whitePanels.stream().mapToLong(Coordinate::x).min().orElseThrow();
        var maxX = whitePanels.stream().mapToLong(Coordinate::x).max().orElseThrow();
        var minY = whitePanels.stream().mapToLong(Coordinate::y).min().orElseThrow();
        var maxY = whitePanels.stream().mapToLong(Coordinate::y).max().orElseThrow();

        return LongStream.rangeClosed(minY, maxY)
                .mapToObj(y -> LongStream.rangeClosed(minX, maxX)
                        .mapToObj(x -> whitePanels.contains(new Coordinate(x, y)) ? "#" : " ")
                        .collect(Collectors.joining()))
                .collect(Collectors.joining("\n"));
    }

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

    public record Coordinate(long x, long y) {

        public Coordinate move(Day11.Direction direction) {
            return switch (direction) {
                case NORTH -> new Coordinate(this.x, this.y - 1);
                case EAST -> new Coordinate(this.x + 1, this.y);
                case SOUTH -> new Coordinate(this.x, this.y + 1);
                case WEST -> new Coordinate(this.x - 1, this.y);
            };
        }
    }

    private static class Hull {

        private final Set<Coordinate> whitePanels = new HashSet<>();
        private final Set<Coordinate> visited = new HashSet<>();

        private void run(List<Long> program) {
            var robot = new Robot(new Coordinate(0, 0), Direction.NORTH);
            var intCode = new IntCode(program);
            var status = intCode.run();

            while (true) {
                switch (status) {
                    case HALTED -> {
                        return;
                    }
                    case INPUT_BLOCKED -> {
                        intCode.input().add(whitePanels.contains(robot.coordinate) ? 1L : 0L);

                        status = intCode.run();

                        paint(robot.coordinate, intCode.output().remove());
                        robot = robot.move(intCode.output().remove());
                    }
                    default -> throw new IllegalStateException("");
                }
            }
        }

        private void paint(Coordinate coordinate, long colour) {
            if (colour == 0) {
                whitePanels.remove(coordinate);
            } else if (colour == 1) {
                whitePanels.add(coordinate);
            } else {
                throw new IllegalStateException("Invalid colour: " + colour);
            }

            visited.add(coordinate);
        }
    }

    private record Robot(Coordinate coordinate, Direction direction) {
        private Robot move(long direction) {
            var newDirection = this.direction;

            if (direction == 0) {
                newDirection = newDirection.turnLeft();
            } else if (direction == 1) {
                newDirection = newDirection.turnRight();
            } else {
                throw new IllegalStateException("Invalid direction: " + direction);
            }

            return new Robot(this.coordinate.move(newDirection), newDirection);
        }
    }
}
