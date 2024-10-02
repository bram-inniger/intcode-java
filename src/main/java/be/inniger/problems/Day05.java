package be.inniger.problems;

import be.inniger.IntCode;

import java.util.List;

public class Day05 {

    public static int partOne(List<Integer> program) {
        var intCode = new IntCode(program);
        intCode.input().add(1);
        intCode.run();

        return intCode.output().stream().filter(out -> out != 0).findFirst().orElseThrow();
    }

    public static int partTwo(List<Integer> program, int input) {
        var intCode = new IntCode(program);
        intCode.input().add(input);
        intCode.run();

        return intCode.output().remove();
    }
}
