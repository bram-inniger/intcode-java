package be.inniger.problems;

import be.inniger.problems.util.InputReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day13Test {

    private static final List<Long> INPUT = InputReader.readProgram("13");

    @Test
    public void partOneSampleTest() {
        // No sample values given
    }

    @Test
    public void partOneActualTest() {
        assertEquals(273, Day13.partOne(INPUT));
    }
}
