package be.inniger.intcode.problems;

import be.inniger.intcode.IntCode;
import be.inniger.intcode.IntCode.Status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day07 {

    public static long partOne(List<Long> program) {
        return bothParts(program, List.of(0L, 1L, 2L, 3L, 4L));
    }

    public static long partTwo(List<Long> program) {
        return bothParts(program, List.of(5L, 6L, 7L, 8L, 9L));
    }

    private static long bothParts(List<Long> program, List<Long> phasesElements) {
        return permutations(phasesElements)
                .stream()
                .mapToLong(phases -> amplify(program, phases))
                .max()
                .orElseThrow();
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

    private static <T> List<List<T>> permutations(List<T> elements) {
        return permutationsHelper(new HashSet<>(elements), List.of());
    }

    private static <T> List<List<T>> permutationsHelper(Set<T> elements, List<T> current) {
        if (elements.isEmpty()) {
            return List.of(current);
        }

        List<List<T>> permutations = new ArrayList<>();

        for (T el : elements) {
            var newEls = new HashSet<>(elements);
            var newCurrent = new ArrayList<>(current);

            newEls.remove(el);
            newCurrent.add(el);

            permutations.addAll(permutationsHelper(newEls, newCurrent));
        }

        return permutations;
    }
}
