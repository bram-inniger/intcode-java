package be.inniger.intcode.problems;

import be.inniger.intcode.IntCode;

import java.util.List;
import java.util.stream.Collectors;

public class Day21 {

    public static long partOne(List<Long> program) {
        // Manually worked out on paper and by looking at failure scenarios
        // If _any_ of the 3 next distances away is a hole AND the 4th one is ground, then jump
        //
        // J = ( (NOT A) OR (NOT B) OR (NOT C) ) AND D
        //
        // which is equivalent to (De Morgan's Law)
        //
        // J = ( NOT ( A AND B AND C ) ) AND D
        //
        // Which translates to the jumpscript
        //
        //  OR  A J  -> J starts at false, so this stores the current value of A into J
        //  AND B J  -> store "B AND J" in J -> "J = B AND A"
        //  AND C J  -> store "C AND J" in J -> "J = C AND B AND A"
        //  NOT J J  -> store the negation of J in J -> "J = NOT ( C AND B AND A )"
        //  AND D J  -> store "D and J" in J -> "J = ( NOT ( C AND B AND A ) ) AND D"
        //  WALK     -> start the program
        var jumpScript = """
                OR  A J
                AND B J
                AND C J
                NOT J J
                AND D J
                WALK
                """.chars().mapToLong(c -> (long) c).boxed().toList();
        var intCode = new IntCode(program);

        jumpScript.forEach(c -> intCode.input().add(c));
        intCode.run();
        var output = intCode.output().stream().toList();

        if (output.getLast() <= Character.MAX_VALUE) {
            var debug = output.stream().map(c -> (char) (long) c).map(Object::toString).collect(Collectors.joining());
            throw new IllegalStateException(debug);
        }

        return output.getLast();
    }
}
