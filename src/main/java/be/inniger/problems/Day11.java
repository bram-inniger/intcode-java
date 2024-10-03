package be.inniger.problems;

import be.inniger.IntCode;
import be.inniger.util.Coordinate;
import be.inniger.util.Direction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day11 {

    public static long partOne(List<Long> program) {
        return new Hull().run(program);
    }

    private static class Hull {

        private final Set<Coordinate> whitePanels = new HashSet<>();
        private final Set<Coordinate> visited = new HashSet<>();

        private long run(List<Long> program) {
            var robot = new Robot(new Coordinate(0, 0), Direction.NORTH);
            var intCode = new IntCode(program);
            var status = intCode.run();

            while (true) {
                switch (status) {
                    case HALTED -> {
                        return visited.size();
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
