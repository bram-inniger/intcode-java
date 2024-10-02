package be.inniger.problems;

import be.inniger.IntCode;

import java.util.List;

public class Day09 {

    public static long partOne(List<Long> program) {
        var intCode = new IntCode(program);
        intCode.input().add(1L);
        intCode.run();
        return intCode.output().remove();
    }
}
