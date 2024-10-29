package be.inniger.intcode.problems;

import be.inniger.intcode.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day23Test {

    private static final List<Long> INPUT = TestUtil.readProgram("23");

    @Test
    public void partOneSampleTest() {
        // No sample values given
    }

    @Test
    public void partOneActualTest() {
        assertEquals(17_283, Day23.partOne(INPUT));
    }

    @Test
    public void partTwoSampleTest() {
        // No sample values given
    }

    @Test
    public void partTwoActualTest() {
        assertEquals(11_319, Day23.partTwo(INPUT));
    }
}
