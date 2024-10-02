package be.inniger.problems;

import be.inniger.IntCode;

import java.util.List;

public class Day05 {

    public static long partOne(List<Long> program) {
        var intCode = new IntCode(program);
        intCode.input().add(1L);
        intCode.run();

        return intCode.output().stream().filter(out -> out != 0).findFirst().orElseThrow();
    }

    public static long partTwo(List<Long> program, long input) {
        var intCode = new IntCode(program);
        intCode.input().add(input);
        intCode.run();

        return intCode.output().remove();
    }
}
