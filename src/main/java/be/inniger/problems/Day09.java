package be.inniger.problems;

import be.inniger.IntCode;

import java.util.List;

public class Day09 {

    public static long partOne(List<Long> program) {
        return bothParts(program, 1L);
    }

    public static long partTwo(List<Long> program) {
        return bothParts(program, 2L);
    }

    private static long bothParts(List<Long> program, long instruction) {
        var intCode = new IntCode(program);
        intCode.input().add(instruction);
        intCode.run();
        return intCode.output().remove();
    }
}
