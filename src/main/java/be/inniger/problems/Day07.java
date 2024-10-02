package be.inniger.problems;

import be.inniger.IntCode;
import be.inniger.Util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Day07 {

    public static int partOne(List<Integer> program) {
        return Util.permutations(List.of(0, 1, 2, 3, 4))
                .stream()
                .mapToInt(phases -> amplify(program, phases))
                .max()
                .orElseThrow();
    }

    private static int amplify(List<Integer> program, List<Integer> phases) {
        var dummyOutput = new ArrayDeque<Integer>();
        var intCodes = new ArrayList<IntCode>();

        Queue<Integer> prevOutput = dummyOutput;
        for (int phase : phases) {
            var output = new ArrayDeque<Integer>();
            var intCode = new IntCode(program, prevOutput, output);

            intCode.input().add(phase);
            prevOutput = intCode.output();

            intCodes.add(intCode);
        }

        dummyOutput.add(0);

        intCodes.forEach(IntCode::run);

        return prevOutput.remove();
    }
}
