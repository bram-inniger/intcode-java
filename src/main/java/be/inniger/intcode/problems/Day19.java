package be.inniger.intcode.problems;

import be.inniger.intcode.IntCode;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day19 {

    public static long partOne(List<Long> program) {
        var area = 50;
        var drone = new Drone(program);
        var tractorBeam = new TractorBeam(drone);

        return tractorBeam.beam(area).size();
    }

    public static long partTwo(List<Long> program) {
        var area = 100;
        var drone = new Drone(program);
        var tractorBeam = new TractorBeam(drone);

        var x = 0L;
        var y = area;

        while (true) {
            // This will be the bottom left point of the square
            var start = tractorBeam.firstPulled(y, x);

            // And this is the top right point
            // Because of the shape of the beam, if both points are inside the beam, the whole square is
            if (tractorBeam.isPulled(new Coordinate(start.x + (area - 1), start.y - (area - 1)))) {
                var first = new Coordinate(start.x, start.y - (area - 1));
                return first.x * 10_000 + first.y;
            }

            // Else try the next line as a starting point
            // Also shift the x-starting point to the new potential minimum, this limits the amount of points to try
            y++;
            x = start.x;
        }
    }

    private record Drone(List<Long> program) {

        private boolean isPulled(Coordinate coordinate) {
            var intCode = new IntCode(program);
            intCode.input().add(coordinate.x);
            intCode.input().add(coordinate.y);

            var status = intCode.run();

            if (status != IntCode.Status.HALTED) {
                throw new IllegalStateException("Program did not complete");
            }

            var output = intCode.output().remove().intValue();

            return switch (output) {
                case 0 -> false;
                case 1 -> true;
                default -> throw new IllegalStateException("Received invalid output: " + output);
            };
        }
    }

    private record TractorBeam(Drone drone) {

        private Set<Coordinate> beam(long area) {
            return LongStream.range(0, area)
                    .boxed()
                    .flatMap(x -> LongStream.range(0, area).mapToObj(y -> new Coordinate(x, y)))
                    .filter(drone::isPulled)
                    .collect(Collectors.toSet());
        }

        private Coordinate firstPulled(long y, long xStart) {
            return LongStream.iterate(xStart, x -> x + 1)
                    .mapToObj(x -> new Coordinate(x, y))
                    .dropWhile(Predicate.not(this::isPulled))
                    .findFirst()
                    .orElseThrow();
        }

        private boolean isPulled(Coordinate coordinate) {
            return drone.isPulled(coordinate);
        }

        @SuppressWarnings("unused")
        private String prettyPrint(long area) {
            var beam = beam(area);

            return LongStream.range(0, area).mapToObj(y -> LongStream.range(0, area).mapToObj(x -> {
                var coordinate = new Coordinate(x, y);
                return beam.contains(coordinate) ? "#" : ".";
            }).collect(Collectors.joining(""))).collect(Collectors.joining("\n"));
        }
    }

    private record Coordinate(long x, long y) {
    }
}
