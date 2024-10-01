package be.inniger.problems;

import be.inniger.problems.util.InputReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day05Test {

    private static final List<Integer> INPUT = InputReader.asSingleInts("05");
    private static final int ID = 5;

    @Test
    public void partOneSampleTest() {
        // No sample values given
    }

    @Test
    public void partOneActualTest() {
        assertEquals(11_049_715, Day05.partOne(INPUT));
    }

    @Test
    public void partTwoSampleTest() {
        // Equal To, position mode
        assertEquals(0, Day05.partTwo(List.of(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), 7));
        assertEquals(1, Day05.partTwo(List.of(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), 8));
        assertEquals(0, Day05.partTwo(List.of(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), 9));

        // Less Than, position mode
        assertEquals(1, Day05.partTwo(List.of(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8), 7));
        assertEquals(0, Day05.partTwo(List.of(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8), 8));
        assertEquals(0, Day05.partTwo(List.of(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8), 9));

        // Equal To, immediate mode
        assertEquals(0, Day05.partTwo(List.of(3, 3, 1108, -1, 8, 3, 4, 3, 99), 7));
        assertEquals(1, Day05.partTwo(List.of(3, 3, 1108, -1, 8, 3, 4, 3, 99), 8));
        assertEquals(0, Day05.partTwo(List.of(3, 3, 1108, -1, 8, 3, 4, 3, 99), 9));

        // Less Than, immediate mode
        assertEquals(1, Day05.partTwo(List.of(3, 3, 1107, -1, 8, 3, 4, 3, 99), 7));
        assertEquals(0, Day05.partTwo(List.of(3, 3, 1107, -1, 8, 3, 4, 3, 99), 8));
        assertEquals(0, Day05.partTwo(List.of(3, 3, 1107, -1, 8, 3, 4, 3, 99), 9));

        // Jump, position mode
        assertEquals(1, Day05.partTwo(List.of(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9), -1));
        assertEquals(0, Day05.partTwo(List.of(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9), 0));
        assertEquals(1, Day05.partTwo(List.of(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9), 1));

        // Jump, immediate mode
        assertEquals(1, Day05.partTwo(List.of(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1), -1));
        assertEquals(0, Day05.partTwo(List.of(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1), 0));
        assertEquals(1, Day05.partTwo(List.of(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1), 1));

        // Larger example
        assertEquals(999, Day05.partTwo(List.of(
                3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
        ), 6));
        assertEquals(999, Day05.partTwo(List.of(
                3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
        ), 7));
        assertEquals(1_000, Day05.partTwo(List.of(
                3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
        ), 8));
        assertEquals(1_001, Day05.partTwo(List.of(
                3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
        ), 9));
        assertEquals(1_001, Day05.partTwo(List.of(
                3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
        ), 10));
    }

    @Test
    public void partTwoActualTest() {
        assertEquals(2_140_710, Day05.partTwo(INPUT, ID));
    }
}
