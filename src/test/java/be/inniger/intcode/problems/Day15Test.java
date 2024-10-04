package be.inniger.intcode.problems;

import be.inniger.intcode.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day15Test {

    private static final List<Long> INPUT = TestUtil.readProgram("15");

    @Test
    public void partOneSampleTest() {
        // No sample values given
    }

    @Test
    public void partOneActualTest() {
        assertEquals(300, Day15.partOne(INPUT));
    }

    @Test
    public void partTwoSampleTest() {
        // No sample values given
    }

    @Test
    public void partTwoActualTest() {
        assertEquals(312, Day15.partTwo(INPUT));
    }
}
