package be.inniger.problems;

import be.inniger.problems.util.InputReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day02Test {

    private static final List<Integer> INPUT = InputReader.asSingleInts("02");

    @Test
    public void partOneSampleTest() {
        assertEquals(3_500, Day02.partOne(List.of(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)));
        assertEquals(2, Day02.partOne(List.of(1, 0, 0, 0, 99)));
        assertEquals(2, Day02.partOne(List.of(2, 3, 0, 3, 99)));
        assertEquals(2, Day02.partOne(List.of(2, 4, 4, 5, 99, 0)));
        assertEquals(30, Day02.partOne(List.of(1, 1, 1, 4, 99, 5, 6, 0, 99)));
    }

    @Test
    public void partOneActualTest() {
        assertEquals(2_894_520, Day02.partOne(INPUT, 12, 2));
    }

    @Test
    public void partTwoSampleTest() {
        // No sample values given
    }

    @Test
    public void partTwoActualTest() {
        assertEquals(9_342, Day02.partTwo(INPUT));
    }
}
