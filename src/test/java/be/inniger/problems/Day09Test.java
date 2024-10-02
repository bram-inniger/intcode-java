package be.inniger.problems;

import be.inniger.problems.util.InputReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static be.inniger.problems.util.Util.asLong;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day09Test {

    private static final List<Long> INPUT = InputReader.readProgram("09");

    @Test
    public void partOneSampleTest() {
        //@formatter:off
        assertEquals(109, Day09.partOne(asLong(List.of(109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99))));
        assertAll(
                () -> assertTrue(1_000_000_000_000_000L < Day09.partOne(asLong(List.of(1102, 34915192, 34915192, 7, 4, 7, 99, 0)))),
                () -> assertTrue(9_999_999_999_999_999L > Day09.partOne(asLong(List.of(1102, 34915192, 34915192, 7, 4, 7, 99, 0))))
        );
        assertEquals(1_125_899_906_842_624L, Day09.partOne(List.of(104L, 1125899906842624L, 99L)));
        //@formatter:on
    }

    @Test
    public void partOneActualTest() {
        assertEquals(3_780_860_499L, Day09.partOne(INPUT));
    }

    @Test
    public void partTwoSampleTest() {
        // No sample values given
    }

    @Test
    public void partTwoActualTest() {
        assertEquals(33_343, Day09.partTwo(INPUT));
    }
}
