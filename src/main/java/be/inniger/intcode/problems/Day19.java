package be.inniger.intcode.problems;

import be.inniger.intcode.IntCode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day19 {

    public static long partOne(List<Long> program) {
        var drone = new Drone(program);
        var tractorBeam = TractorBeam.of(drone);

        return tractorBeam.beam.size();
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

    private record TractorBeam(Set<Coordinate> beam) {

        private static final long AREA = 50;

        private static TractorBeam of(Drone drone) {
            var beam = new HashSet<Coordinate>();

            for (long x = 0; x < AREA; x++) {
                for (long y = 0; y < AREA; y++) {
                    var coordinate = new Coordinate(x, y);

                    if (drone.isPulled(coordinate)) {
                        beam.add(coordinate);
                    }
                }
            }

            return new TractorBeam(beam);
        }

        @SuppressWarnings("unused")
        private String prettyPrint() {
            return LongStream.range(0, AREA).mapToObj(y ->
                    LongStream.range(0, AREA).mapToObj(x -> {
                        var coordinate = new Coordinate(x, y);
                        return beam.contains(coordinate) ? "#" : ".";
                    }).collect(Collectors.joining(""))
            ).collect(Collectors.joining("\n"));
        }
    }

    private record Coordinate(long x, long y) {
    }
}
