package be.inniger.problems;

import be.inniger.IntCode;

import java.util.List;

public class Day02 {
    public static int partOne(List<Integer> program) {
        var intCode = new IntCode(program);

        while (!intCode.halted()) {
            intCode.run();
        }

        return intCode.memZero();
    }
}
