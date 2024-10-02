package be.inniger.problems;

import be.inniger.IntCode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day02 {
    private static final long DESIRED_OUTPUT = 19_690_720;

    public static long partOne(List<Long> program) {
        var intCode = new IntCode(program);
        intCode.run();
        return getFirstMemoryElement(intCode);
    }

    public static long partOne(List<Long> program, long replaceOne, long replaceTwo) {
        return partOne(overrideProgram(program, replaceOne, replaceTwo));
    }

    public static long partTwo(List<Long> program) {
        for (long noun = 0; noun <= 99; noun++) {
            for (long verb = 0; verb <= 99; verb++) {
                var programCpy = overrideProgram(program, noun, verb);
                var intCode = new IntCode(programCpy);
                intCode.run();

                var output = getFirstMemoryElement(intCode);

                if (output == DESIRED_OUTPUT) {
                    return 100 * noun + verb;
                }
            }
        }

        throw new IllegalStateException("No suitable combination found");
    }

    private static List<Long> overrideProgram(List<Long> program, long noun, long verb) {
        var programCpy = new ArrayList<>(program);
        programCpy.set(1, noun);
        programCpy.set(2, verb);

        return programCpy;
    }

    // This allows the API of the IntCode _not_ to expose its internal memory
    @SuppressWarnings("unchecked")
    private static long getFirstMemoryElement(IntCode intCode) {
        try {
            Field memory = intCode.getClass().getDeclaredField("mem");
            memory.setAccessible(true);
            Map<Long, Long> mem = (Map<Long, Long>) memory.get(intCode);
            return mem.get(0L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
