package be.inniger.intcode.problems;

import be.inniger.intcode.IntCode;

import java.util.List;
import java.util.stream.Collectors;

public class Day21 {

    // If _any_ of the 3 next distances away (A, B, C) is a hole AND the 4th one (D) is ground, then jump
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
    private static final String WALK_JUMP_SCRIPT = """
            OR  A J
            AND B J
            AND C J
            NOT J J
            AND D J
            WALK
            """;

    // If _any_ of the 3 next distances away (A, B, C) is a hole AND the 4th one (D) is ground, then jump,
    // if and only if the next move-distance (E) or the next jump-distance (H) is ground.
    //
    // J = ( (NOT A) OR (NOT B) OR (NOT C) ) AND ( D ) AND ( E OR H )
    //
    // which is equivalent to (De Morgan's Law)
    //
    // J = ( NOT ( A AND B AND C ) ) AND ( D ) AND ( E OR H )
    //
    // Which translates to the jumpscript
    //
    //  OR  A J  -> J starts at false, so this stores the current value of A into J
    //  AND B J  -> store "B AND J" in J -> "J = B AND A"
    //  AND C J  -> store "C AND J" in J -> "J = C AND B AND A"
    //  NOT J J  -> store the negation of J in J -> "J = NOT ( C AND B AND A )"
    //  AND D J  -> store "D and J" in J -> "J = D AND ( NOT ( C AND B AND A ) )"
    //  OR  E T  -> store E in T -> "J = D AND ( NOT ( C AND B AND A ) )", "T = E"
    //  OR  H T  -> store "H OR T" in T -> "J = D AND ( NOT ( C AND B AND A ) )", "T = H OR E"
    //  AND T J  -> store "T AND J" in J -> "J = (H OR E) AND D AND ( NOT ( C AND B AND A ) )"
    //  RUN      -> start the program
    private static final String RUN_JUMP_SCRIPT = """
            OR  A J
            AND B J
            AND C J
            NOT J J
            AND D J
            OR  E T
            OR  H T
            AND T J
            RUN
            """;

    public static long partOne(List<Long> program) {
        return run(program, WALK_JUMP_SCRIPT);
    }


    public static long partTwo(List<Long> program) {
        return run(program, RUN_JUMP_SCRIPT);
    }

    private static long run(List<Long> program, String jumpScript) {
        var intCode = new IntCode(program);

        jumpScript.chars().mapToLong(c -> (long) c).forEach(c -> intCode.input().add(c));
        intCode.run();
        var output = intCode.output().stream().toList();

        if (output.getLast() <= Character.MAX_VALUE) {
            var debug = output.stream().map(c -> (char) (long) c).map(Object::toString).collect(Collectors.joining());
            throw new IllegalStateException(debug);
        }

        return output.getLast();
    }
}
