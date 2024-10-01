package be.inniger.problems;

import be.inniger.IntCode;

import java.util.ArrayDeque;
import java.util.List;

public class Day05 {

    public static int partOne(List<Integer> program) {
        var inputs = new ArrayDeque<>(List.of(1));
        var outputs = new ArrayDeque<Integer>();

        new IntCode(program, inputs, outputs).run();

        return outputs.stream().filter(out -> out != 0).findFirst().orElseThrow();
    }
}
