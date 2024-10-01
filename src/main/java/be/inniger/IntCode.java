package be.inniger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class IntCode {

    private final List<Integer> mem;
    private final Queue<Integer> inputs;
    private final Queue<Integer> outputs;
    private int addr;

    public IntCode(List<Integer> program, Queue<Integer> inputs, Queue<Integer> outputs) {
        this.mem = new ArrayList<>(program);
        this.inputs = inputs;
        this.outputs = outputs;
        this.addr = 0;
    }

    public List<Integer> run() {
        while (notHalted()) {
            step();
        }

        return new ArrayList<>(mem);
    }

    private void step() {
        var opCode = OpCode.of(mem.get(addr) % 100);
        var params = params(opCode);

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
            case HALT -> {
                // No-op
            }
        }

        addr += opCode.increment;
    }

    private List<Integer> params(OpCode opCode) {
        var modes = mem.get(addr) / 100;

        return switch (opCode) {
            case ADD, MULTIPLY -> List.of(
                    modes % 10 == 0 ? mem.get(mem.get(addr + 1)) : mem.get(addr + 1),
                    (modes / 10) % 10 == 0 ? mem.get(mem.get(addr + 2)) : mem.get(addr + 2),
                    mem.get(addr + 3)
            );
            case INPUT -> List.of(
                    mem.get(addr + 1)
            );
            case OUTPUT -> List.of(
                    modes % 10 == 0 ? mem.get(mem.get(addr + 1)) : mem.get(addr + 1)
            );
            case HALT -> List.of();
        };
    }

    private boolean notHalted() {
        return addr >= 0 && addr < mem.size() && mem.get(addr) != OpCode.HALT.code;
    }

    private enum OpCode {
        ADD(1, 4),
        MULTIPLY(2, 4),
        INPUT(3, 2),
        OUTPUT(4, 2),
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
