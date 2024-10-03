package be.inniger.intcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class TestUtil {

    public static List<Long> readProgram(String day) {
        var path = Paths.get("./src/main/resources/inputs/day" + day + ".txt");

        try (var lines = Files.lines(path)) {
            var ints = lines.findFirst().orElseThrow().split(",");
            return Arrays.stream(ints).map(Long::parseLong).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Long> asLong(List<Integer> list) {
        return list.stream().map(el -> (long) el).toList();
    }
}
