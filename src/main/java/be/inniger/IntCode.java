package be.inniger;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class IntCode {

    private final List<Integer> mem;
    private Queue<Integer> inputs;
    private Queue<Integer> outputs;

    private int ipr;

    public IntCode(List<Integer> program) {
        this.mem = new ArrayList<>(program);
        this.inputs = new ArrayDeque<>();
        this.outputs = new ArrayDeque<>();
        this.ipr = 0;
    }

    public Status run() {
        var status = Status.RUNNING;

        while (status == Status.RUNNING) {
            status = step();
        }

        return status;
    }

    public Queue<Integer> input() {
        return inputs;
    }

    public Queue<Integer> output() {
        return outputs;
    }

    @SuppressWarnings("unused") // Here to mirror the equivalent output method
    public void wireInput(Queue<Integer> inputs) {
        this.inputs = inputs;
    }

    public void wireOutput(Queue<Integer> outputs) {
        this.outputs = outputs;
    }

    public List<Integer> memory() {
        return new ArrayList<>(mem);
    }

    private Status step() {
        var opCode = OpCode.of(mem.get(ipr) % 100);
        var params = params(opCode);
        var shouldIncrement = true;

        switch (opCode) {
            case ADD -> mem.set(params.get(2), params.get(0) + params.get(1));
            case MULTIPLY -> mem.set(params.get(2), params.get(0) * params.get(1));
            case INPUT -> {
                if (inputs.isEmpty()) {
                    return Status.INPUT_BLOCKED;
                }

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
            case LESS_THAN -> mem.set(params.get(2), params.get(0) < params.get(1) ? 1 : 0);
            case EQUALS -> mem.set(params.get(2), (int) params.get(0) == params.get(1) ? 1 : 0);
            case HALT -> {
                return Status.HALTED;
            }
        }

        if (shouldIncrement) {
            ipr += opCode.increment;
        }

        return Status.RUNNING;
    }

    private List<Integer> params(OpCode opCode) {
        var modes = mem.get(ipr) / 100;

        return switch (opCode) {
            case ADD, MULTIPLY, LESS_THAN, EQUALS ->
                    List.of(modes % 10 == 0 ? mem.get(mem.get(ipr + 1)) : mem.get(ipr + 1), (modes / 10) % 10 == 0 ? mem.get(mem.get(ipr + 2)) : mem.get(ipr + 2), mem.get(ipr + 3));
            case INPUT -> List.of(mem.get(ipr + 1));
            case OUTPUT -> List.of(modes % 10 == 0 ? mem.get(mem.get(ipr + 1)) : mem.get(ipr + 1));
            case JUMP_IF_TRUE, JUMP_IF_FALSE ->
                    List.of(modes % 10 == 0 ? mem.get(mem.get(ipr + 1)) : mem.get(ipr + 1), (modes / 10) % 10 == 0 ? mem.get(mem.get(ipr + 2)) : mem.get(ipr + 2));
            case HALT -> List.of();
        };
    }

    public enum Status {
        RUNNING, INPUT_BLOCKED, HALTED,
    }

    private enum OpCode {
        ADD(1, 4), MULTIPLY(2, 4), INPUT(3, 2), OUTPUT(4, 2), JUMP_IF_TRUE(5, 3), JUMP_IF_FALSE(6, 3), LESS_THAN(7, 4), EQUALS(8, 4), HALT(99, 0),
        ;

        private final int code;
        private final int increment;

        OpCode(int code, int increment) {
            this.code = code;
            this.increment = increment;
        }

        private static OpCode of(int code) {
            return Arrays.stream(OpCode.values()).filter(opCode -> opCode.code == code).findFirst().orElseThrow();
        }
    }
}
