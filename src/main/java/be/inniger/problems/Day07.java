package be.inniger.problems;

import be.inniger.IntCode;
import be.inniger.IntCode.Status;
import be.inniger.Util;

import java.util.List;

public class Day07 {

    public static long partOne(List<Long> program) {
        return bothParts(program, List.of(0L, 1L, 2L, 3L, 4L));
    }

    public static long partTwo(List<Long> program) {
        return bothParts(program, List.of(5L, 6L, 7L, 8L, 9L));
    }

    private static long bothParts(List<Long> program, List<Long> phasesElements) {
        return Util.permutations(phasesElements).stream().mapToLong(phases -> amplify(program, phases)).max().orElseThrow();
    }

    private static long amplify(List<Long> program, List<Long> phases) {
        // Create IntCode computers, with the phase as their first input
        var intCodes = phases.stream().map(phase -> phasedIntCode(program, phase)).toList();

        // Wire the output of one to the input of the next, circular
        for (int i = 0; i < intCodes.size(); i++) {
            var intCode = intCodes.get(i);
            intCode.wireOutput(intCodes.get((i + 1) % intCodes.size()).input());
        }

        // Additionally, once, give the value 0 as input to the first computer
        intCodes.getFirst().input().add(0L);

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

    private static IntCode phasedIntCode(List<Long> program, Long phase) {
        var intCode = new IntCode(program);
        intCode.input().add(phase);
        return intCode;
    }
}
