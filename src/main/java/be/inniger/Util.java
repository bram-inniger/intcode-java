package be.inniger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Util {

    public static <T> List<List<T>> permutations(List<T> elements) {
        return permutationsHelper(new HashSet<>(elements), List.of());
    }

    private static <T> List<List<T>> permutationsHelper(Set<T> elements, List<T> current) {
        if (elements.isEmpty()) {
            return List.of(current);
        }

        List<List<T>> permutations = new ArrayList<>();

        for (T el : elements) {
            var newEls = new HashSet<>(elements);
            var newCurrent = new ArrayList<>(current);

            newEls.remove(el);
            newCurrent.add(el);

            permutations.addAll(permutationsHelper(newEls, newCurrent));
        }

        return permutations;
    }
}
