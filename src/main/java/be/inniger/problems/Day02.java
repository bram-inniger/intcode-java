package be.inniger.problems;

import be.inniger.IntCode;

import java.util.ArrayList;
import java.util.List;

public class Day02 {
    private static final int DESIRED_OUTPUT = 19_690_720;

    public static int partOne(List<Integer> program) {
        var intCode = new IntCode(program);
        intCode.run();
        return intCode.memory().getFirst();
    }

    public static int partOne(List<Integer> program, int replaceOne, int replaceTwo) {
        return partOne(overrideProgram(program, replaceOne, replaceTwo));
    }

    public static int partTwo(List<Integer> program) {
        for (int noun = 0; noun <= 99; noun++) {
            for (int verb = 0; verb <= 99; verb++) {
                var programCpy = overrideProgram(program, noun, verb);
                var intCode = new IntCode(programCpy);
                intCode.run();

                var output = intCode.memory().getFirst();

                if (output == DESIRED_OUTPUT) {
                    return 100 * noun + verb;
                }
            }
        }

        throw new IllegalStateException("No suitable combination found");
    }

    private static List<Integer> overrideProgram(List<Integer> program, int noun, int verb) {
        var programCpy = new ArrayList<>(program);
        programCpy.set(1, noun);
        programCpy.set(2, verb);

        return programCpy;
    }
}
