package be.inniger.problems.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InputReader {

    public static List<Integer> asSingleInts(String day) {
        var path = Paths.get("./src/main/resources/inputs/day" + day + ".txt");

        try (var lines = Files.lines(path)) {
            var ints = lines.findFirst().orElseThrow().split(",");
            return Arrays.stream(ints).map(Integer::parseInt).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
