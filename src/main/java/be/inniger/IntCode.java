package be.inniger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class IntCode {

    private final List<Integer> mem;
    private final Queue<Integer> inputs;
    private final Queue<Integer> outputs;

    private int ipr;

    public IntCode(List<Integer> program, Queue<Integer> inputs, Queue<Integer> outputs) {
        this.mem = new ArrayList<>(program);
        this.inputs = inputs;
        this.outputs = outputs;
        this.ipr = 0;
    }

    public List<Integer> run() {
        while (notHalted()) {
            step();
        }

        return new ArrayList<>(mem);
    }

    public Queue<Integer> input() {
        return inputs;
    }

    public Queue<Integer> output() {
        return outputs;
    }

    private void step() {
        var opCode = OpCode.of(mem.get(ipr) % 100);
        var params = params(opCode);
        var shouldIncrement = true;

        switch (opCode) {
            case ADD -> mem.set(params.get(2), params.get(0) + params.get(1));
            case MULTIPLY -> mem.set(params.get(2), params.get(0) * params.get(1));
            case INPUT -> {
                var input = inputs.remove();
                mem.set(params.getFirst(), input);
            }
            case OUTPUT -> {
                var output = params.getFirst();
                outputs.add(output);
            }
            case JUMP_IF_TRUE -> {
                if (params.getFirst() != 0) {
                    ipr = params.get(1);
                    shouldIncrement = false;
                }
            }
            case JUMP_IF_FALSE -> {
                if (params.getFirst() == 0) {
                    ipr = params.get(1);
                    shouldIncrement = false;
                }
            }
            case LESS_THAN -> mem.set(
                    params.get(2),
                    params.get(0) < params.get(1) ? 1 : 0
            );
            case EQUALS -> mem.set(
                    params.get(2),
                    (int) params.get(0) == params.get(1) ? 1 : 0
            );
            case HALT -> {
                // No-op
            }
        }

        if (shouldIncrement) {
            ipr += opCode.increment;
        }
    }

    private List<Integer> params(OpCode opCode) {
        var modes = mem.get(ipr) / 100;

        return switch (opCode) {
            case ADD, MULTIPLY, LESS_THAN, EQUALS -> List.of(
                    modes % 10 == 0 ? mem.get(mem.get(ipr + 1)) : mem.get(ipr + 1),
                    (modes / 10) % 10 == 0 ? mem.get(mem.get(ipr + 2)) : mem.get(ipr + 2),
                    mem.get(ipr + 3)
            );
            case INPUT -> List.of(
                    mem.get(ipr + 1)
            );
            case OUTPUT -> List.of(
                    modes % 10 == 0 ? mem.get(mem.get(ipr + 1)) : mem.get(ipr + 1)
            );
            case JUMP_IF_TRUE, JUMP_IF_FALSE -> List.of(
                    modes % 10 == 0 ? mem.get(mem.get(ipr + 1)) : mem.get(ipr + 1),
                    (modes / 10) % 10 == 0 ? mem.get(mem.get(ipr + 2)) : mem.get(ipr + 2)
            );
            case HALT -> List.of();
        };
    }

    private boolean notHalted() {
        return ipr >= 0 && ipr < mem.size() && mem.get(ipr) != OpCode.HALT.code;
    }

    private enum OpCode {
        ADD(1, 4),
        MULTIPLY(2, 4),
        INPUT(3, 2),
        OUTPUT(4, 2),
        JUMP_IF_TRUE(5, 3),
        JUMP_IF_FALSE(6, 3),
        LESS_THAN(7, 4),
        EQUALS(8, 4),
        HALT(99, 0),
        ;

        private final int code;
        private final int increment;

        OpCode(int code, int increment) {
            this.code = code;
            this.increment = increment;
        }

        private static OpCode of(int code) {
            return Arrays.stream(OpCode.values())
                    .filter(opCode -> opCode.code == code)
                    .findFirst()
                    .orElseThrow();
        }
    }
}
