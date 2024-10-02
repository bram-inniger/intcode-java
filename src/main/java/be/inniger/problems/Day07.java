package be.inniger.problems;

import be.inniger.IntCode;
import be.inniger.IntCode.Status;
import be.inniger.Util;

import java.util.List;

public class Day07 {

    public static int partOne(List<Integer> program) {
        return bothParts(program, List.of(0, 1, 2, 3, 4));
    }

    public static int partTwo(List<Integer> program) {
        return bothParts(program, List.of(5, 6, 7, 8, 9));
    }

    private static int bothParts(List<Integer> program, List<Integer> phasesElements) {
        return Util.permutations(phasesElements).stream().mapToInt(phases -> amplify(program, phases)).max().orElseThrow();
    }

    private static int amplify(List<Integer> program, List<Integer> phases) {
        // Create IntCode computers, with the phase as their first input
        var intCodes = phases.stream().map(phase -> phasedIntCode(program, phase)).toList();

        // Wire the output of one to the input of the next, circular
        for (int i = 0; i < intCodes.size(); i++) {
            var intCode = intCodes.get(i);
            intCode.wireOutput(intCodes.get((i + 1) % intCodes.size()).input());
        }

        // Additionally, once, give the value 0 as input to the first computer
        intCodes.getFirst().input().add(0);

        // Keep running the computers until the last one has halted
        var status = Status.RUNNING;
        while (status != Status.HALTED) {
            for (var intCode : intCodes) {
                status = intCode.run();
            }
        }

        // Last computer's last output is the final result
        return intCodes.getLast().output().remove();
    }

    private static IntCode phasedIntCode(List<Integer> program, Integer phase) {
        var intCode = new IntCode(program);
        intCode.input().add(phase);
        return intCode;
    }
}
