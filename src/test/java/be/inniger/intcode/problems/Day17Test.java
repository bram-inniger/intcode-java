package be.inniger.intcode.problems;

import be.inniger.intcode.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day17Test {

    private static final List<Long> INPUT = TestUtil.readProgram("17");

    @Test
    public void partOneSampleTest() {
        // No sample values given
    }

    @Test
    public void partOneActualTest() {
        assertEquals(10_632, Day17.partOne(INPUT));
    }

    @Test
    public void partTwoSampleTest() {
        // No sample values given
    }

    @Test
    public void partTwoActualTest() {
        assertEquals(1_356_191, Day17.partTwo(INPUT));
    }
}
