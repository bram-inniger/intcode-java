package be.inniger.intcode.problems;

import be.inniger.intcode.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day21Test {

    private static final List<Long> INPUT = TestUtil.readProgram("21");

    @Test
    public void partOneSampleTest() {
        // No sample values given
    }

    @Test
    public void partOneActualTest() {
        assertEquals(19_352_638, Day21.partOne(INPUT));
    }

    @Test
    public void partTwoSampleTest() {
        // No sample values given
    }

    @Test
    public void partTwoActualTest() {
        assertEquals(1_141_251_258, Day21.partTwo(INPUT));
    }
}
