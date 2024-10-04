package be.inniger.intcode.problems;

import be.inniger.intcode.IntCode;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day17 {

    public static long partOne(List<Long> program) {
        var intCode = new IntCode(program);
        var status = intCode.run();

        if (status != IntCode.Status.HALTED) {
            throw new IllegalStateException("Program did not complete");
        }

        var lines = intCode.output()
                .stream()
                .map(l -> (char) (long) l)
                .map(String::valueOf)
                .collect(Collectors.joining())
                .split("\n");

        var scaffolds = IntStream.range(0, lines.length)
                .boxed()
                .flatMap(y -> IntStream.range(0, lines[0].length()).mapToObj(x -> switch (lines[y].charAt(x)) {
                    case '#', '^', 'v', '<', '>' -> Optional.of(new Coordinate(x, y));
                    default -> Optional.<Coordinate>empty();
                }))
                .flatMap(Optional::stream)
                .collect(Collectors.toUnmodifiableSet());

        var intersections = scaffolds.stream()
                .filter(scaffold -> scaffolds.containsAll(scaffold.neighbours()))
                .toList();

        return intersections.stream().mapToInt(i -> i.x * i.y).sum();
    }

    private record Coordinate(int x, int y) {

        private List<Coordinate> neighbours() {
            return List.of(new Coordinate(this.x + 1, this.y), new Coordinate(this.x, this.y + 1), new Coordinate(this.x - 1, this.y), new Coordinate(this.x, this.y - 1));
        }
    }
}
