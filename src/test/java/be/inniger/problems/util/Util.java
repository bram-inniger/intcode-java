package be.inniger.problems.util;

import java.util.List;

public class Util {
    public static List<Long> asLong(List<Integer> list) {
        return list.stream().map(el -> (long) el).toList();
    }
}
