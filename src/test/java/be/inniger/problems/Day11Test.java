package be.inniger.problems;

import be.inniger.problems.util.InputReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day11Test {

    private static final List<Long> INPUT = InputReader.readProgram("11");

    @Test
    public void partOneSampleTest() {
        // No sample values given
    }

    @Test
    public void partOneActualTest() {
        assertEquals(2_211, Day11.partOne(INPUT));
    }
}
