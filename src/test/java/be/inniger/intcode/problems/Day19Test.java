package be.inniger.intcode.problems;

import be.inniger.intcode.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day19Test {

    private static final List<Long> INPUT = TestUtil.readProgram("19");

    @Test
    public void partOneSampleTest() {
        // No sample values given
    }

    @Test
    public void partOneActualTest() {
        assertEquals(203, Day19.partOne(INPUT));
    }
}
