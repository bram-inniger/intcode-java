package be.inniger.intcode.problems;

import be.inniger.intcode.IntCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day17 {

    public static long partOne(List<Long> program) {
        return Scaffold.of(program).detectIntersections().stream().mapToInt(i -> i.x * i.y).sum();
    }

    public static long partTwo(List<Long> program) {
        // Printed path:
        // L,12,L,8,R,12,L,10,L,8,L,12,R,12,L,12,L,8,R,12,R,12,L,8,L,10,L,12,L,8,R,12,L,12,L,8,R,12,R,12,L,8,L,10,L,10,L,8,L,12,R,12,R,12,L,8,L,10,L,10,L,8,L,12,R,12,

        // Manually discovered the 3 functions on sight, automate this, TODO
        // A -> L,12,L,8,R,12
        // B -> L,10,L,8,L,12,R,12
        // C -> R,12,L,8,L,10

        // Thus replacing the functions in the path above the main routine is found, automate this, TODO
        // A,B,A,C,A,A,C,B,C,B

        var feed = """
                A,B,A,C,A,A,C,B,C,B
                L,12,L,8,R,12
                L,10,L,8,L,12,R,12
                R,12,L,8,L,10
                n
                """;

        var modified = new ArrayList<>(program);
        modified.set(0, 2L); // Wake up the robot
        var intCode = new IntCode(modified);

        feed.chars().mapToObj(c -> (long) c).forEach(intCode.input()::add);

        var status = intCode.run();

        if (status != IntCode.Status.HALTED) {
            throw new IllegalStateException("The IntCode program did not complete as expected");
        }

        return intCode.output().stream().toList().getLast();
    }

    private record Scaffold(String view, Set<Coordinate> scaffolds, Point start) {

        public static Scaffold of(List<Long> program) {
            var view = constructView(program);
            var scaffolds = detectScaffolds(view);
            var start = Point.of(view);

            return new Scaffold(view, scaffolds, start);
        }

        private static String constructView(List<Long> program) {
            var intCode = new IntCode(program);
            var status = intCode.run();

            if (status != IntCode.Status.HALTED) {
                throw new IllegalStateException("Program did not complete");
            }

            return intCode.output()
                    .stream()
                    .map(l -> (char) (long) l)
                    .map(String::valueOf)
                    .collect(Collectors.joining());
        }

        private static Set<Coordinate> detectScaffolds(String view) {
            var lines = view.split("\n");

            return IntStream.range(0, lines.length)
                    .boxed()
                    .flatMap(y -> IntStream.range(0, lines[0].length()).mapToObj(x -> switch (lines[y].charAt(x)) {
                        case '#', '^', 'v', '<', '>' -> Optional.of(new Coordinate(x, y));
                        default -> Optional.<Coordinate>empty();
                    }))
                    .flatMap(Optional::stream)
                    .collect(Collectors.toUnmodifiableSet());
        }

        public List<Coordinate> detectIntersections() {
            return scaffolds.stream()
                    .filter(scaffold -> scaffold.neighbours()
                            .stream()
                            .map(Point::coordinate)
                            .allMatch(scaffolds::contains))
                    .sorted(Comparator.comparing(Coordinate::y).thenComparing(Coordinate::x))
                    .toList();
        }

        @SuppressWarnings("unused") // Will be used in automation, TODO
        public String calculatePath() {
            var path = new StringBuilder();
            var robot = start;
            var steps = 0;

            while (true) {
                var nextNeighbours = robot.nextNeighbours()
                        .stream()
                        .filter(neighbour -> scaffolds.contains(neighbour.coordinate))
                        .toList();

                switch (nextNeighbours.size()) {
                    case 0 -> {
                        return path.append(steps).append(',').toString();
                    }
                    case 1 -> {
                        var next = nextNeighbours.getFirst();
                        if (next.direction != robot.direction) {
                            if (steps > 0) {
                                path.append(steps).append(',');
                                steps = 0;
                            }

                            switch (robot.direction) {
                                case UP -> {
                                    switch (next.direction) {
                                        case RIGHT -> path.append('R');
                                        case LEFT -> path.append('L');
                                        default -> throw new IllegalStateException("Unreachable");
                                    }
                                }
                                case RIGHT -> {
                                    switch (next.direction) {
                                        case DOWN -> path.append('R');
                                        case UP -> path.append('L');
                                        default -> throw new IllegalStateException("Unreachable");
                                    }
                                }
                                case DOWN -> {
                                    switch (next.direction) {
                                        case LEFT -> path.append('R');
                                        case RIGHT -> path.append('L');
                                        default -> throw new IllegalStateException("Unreachable");
                                    }
                                }
                                case LEFT -> {
                                    switch (next.direction) {
                                        case UP -> path.append('R');
                                        case DOWN -> path.append('L');
                                        default -> throw new IllegalStateException("Unreachable");
                                    }
                                }
                            }
                            path.append(',');
                        }
                        robot = new Point(robot.coordinate.move(next.direction), next.direction);
                        steps++;
                    }
                    case 3 -> {
                        robot = new Point(robot.coordinate.move(robot.direction), robot.direction);
                        steps++;
                    }
                    default -> throw new IllegalStateException("Can never have 2 neighbours (or more than 3)");
                }
            }
        }

        private enum Direction {
            //@formatter:off
            UP('^'),
            RIGHT('>'),
            DOWN('v'),
            LEFT('<'),
            ;
            //@formatter:on

            private final char print;

            Direction(char print) {
                this.print = print;
            }

            public static Direction of(char print) {
                return Arrays.stream(Direction.values())
                        .filter(direction -> direction.print == print)
                        .findFirst()
                        .orElseThrow();
            }

            public Direction opposite() {
                return switch (this) {
                    case UP -> DOWN;
                    case RIGHT -> LEFT;
                    case DOWN -> UP;
                    case LEFT -> RIGHT;
                };
            }
        }

        private record Coordinate(int x, int y) {

            public Coordinate move(Direction direction) {
                return switch (direction) {
                    case UP -> new Coordinate(this.x, this.y - 1);
                    case RIGHT -> new Coordinate(this.x + 1, this.y);
                    case DOWN -> new Coordinate(this.x, this.y + 1);
                    case LEFT -> new Coordinate(this.x - 1, this.y);
                };
            }

            public List<Point> neighbours() {
                return Arrays.stream(Direction.values())
                        .map(direction -> new Point(this.move(direction), direction))
                        .toList();
            }
        }

        private record Point(Coordinate coordinate, Direction direction) {

            public static Point of(String view) {
                var lines = view.split("\n");

                var coordinate = IntStream.range(0, lines.length)
                        .boxed()
                        .flatMap(y -> IntStream.range(0, lines[0].length()).mapToObj(x -> switch (lines[y].charAt(x)) {
                            case '^', 'v', '<', '>' -> Optional.of(new Coordinate(x, y));
                            default -> Optional.<Coordinate>empty();
                        }))
                        .flatMap(Optional::stream)
                        .findFirst()
                        .orElseThrow();
                var direction = Direction.of(lines[coordinate.y].charAt(coordinate.x));

                return new Point(coordinate, direction);
            }

            // Return all neighbours, excluding the previous position
            private List<Point> nextNeighbours() {
                return coordinate.neighbours()
                        .stream()
                        .filter(neighbour -> neighbour.direction != direction.opposite())
                        .toList();
            }
        }
    }
}
