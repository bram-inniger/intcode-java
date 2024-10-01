package be.inniger.problems;

import be.inniger.problems.util.InputReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day05Test {

    @Test
    public void partOneSampleTest() {
        // No sample values given
    }

    @Test
    public void partOneActualTest() {
        var input = InputReader.asSingleInts("05");
        assertEquals(11_049_715, Day05.partOne(input));
    }
}
