package be.inniger;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntCode {

    private final Map<Long, Long> mem;

    private Queue<Long> inputs;
    private Queue<Long> outputs;
    private long ipr;

    public IntCode(List<Long> program) {
        this.mem = IntStream.range(0, program.size()).boxed().collect(Collectors.toMap(i -> (long) i, program::get));
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

    public Queue<Long> input() {
        return inputs;
    }

    public Queue<Long> output() {
        return outputs;
    }

    @SuppressWarnings("unused") // Here to mirror the equivalent output method
    public void wireInput(Queue<Long> inputs) {
        this.inputs = inputs;
    }

    public void wireOutput(Queue<Long> outputs) {
        this.outputs = outputs;
    }

    private Status step() {
        var opCode = OpCode.of(mem.get(ipr) % 100);
        var params = params(opCode);
        var shouldIncrement = true;

        switch (opCode) {
            case ADD -> mem.put(params.get(2), params.get(0) + params.get(1));
            case MULTIPLY -> mem.put(params.get(2), params.get(0) * params.get(1));
            case INPUT -> {
                if (inputs.isEmpty()) {
                    return Status.INPUT_BLOCKED;
                }

                var input = inputs.remove();
                mem.put(params.getFirst(), input);
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
            case LESS_THAN -> mem.put(params.get(2), params.get(0) < params.get(1) ? 1L : 0L);
            case EQUALS -> mem.put(params.get(2), params.get(0).equals(params.get(1)) ? 1L : 0L);
            case HALT -> {
                return Status.HALTED;
            }
        }

        if (shouldIncrement) {
            ipr += opCode.increment;
        }

        return Status.RUNNING;
    }

    private List<Long> params(OpCode opCode) {
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
        //@formatter:off
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
        //@formatter:on

        private final long code;
        private final long increment;

        OpCode(long code, long increment) {
            this.code = code;
            this.increment = increment;
        }

        private static OpCode of(long code) {
            return Arrays.stream(OpCode.values()).filter(opCode -> opCode.code == code).findFirst().orElseThrow();
        }
    }
}
