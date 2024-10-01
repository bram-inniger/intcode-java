package be.inniger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntCode {

    private final List<Integer> mem;
    private int addr;

    public IntCode(List<Integer> program) {
        this.mem = new ArrayList<>(program);
        this.addr = 0;
    }

    public List<Integer> run() {
        while (notHalted()) {
            step();
        }

        return new ArrayList<>(mem);
    }

    private void step() {
        var opCode = OpCode.of(mem.get(addr));
        var inOne = mem.get(mem.get(addr + 1));
        var inTwo = mem.get(mem.get(addr + 2));
        var out = mem.get(addr + 3);

        switch (opCode) {
            case ADD -> {
                mem.set(out, inOne + inTwo);
                addr += 4;
            }
            case MUL -> {
                mem.set(out, inOne * inTwo);
                addr += 4;
            }
            case HALT -> {
            }
        }
    }

    private boolean notHalted() {
        return addr >= 0 && addr < mem.size() && mem.get(addr) != OpCode.HALT.code;
    }

    private enum OpCode {
        ADD(1),
        MUL(2),
        HALT(99),
        ;

        private final int code;

        OpCode(int code) {
            this.code = code;
        }

        private static OpCode of(int code) {
            return Arrays.stream(OpCode.values())
                    .filter(opCode -> opCode.code == code)
                    .findFirst()
                    .orElseThrow();
        }
    }
}
