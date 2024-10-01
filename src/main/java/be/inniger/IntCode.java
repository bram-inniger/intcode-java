package be.inniger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntCode {

    private final List<Integer> mem;
    private int ptr;

    public IntCode(List<Integer> program) {
        this.mem = new ArrayList<>(program);
        this.ptr = 0;
    }

    public void run() {
        var opCode = OpCode.of(mem.get(ptr));

        switch (opCode) {
            case ADD -> {
                var arg1 = mem.get(mem.get(ptr + 1));
                var arg2 = mem.get(mem.get(ptr + 2));
                var arg3 = mem.get(ptr + 3);

                mem.set(arg3, arg1 + arg2);
                ptr += 4;
            }
            case MUL -> {
                var arg1 = mem.get(mem.get(ptr + 1));
                var arg2 = mem.get(mem.get(ptr + 2));
                var arg3 = mem.get(ptr + 3);

                mem.set(arg3, arg1 * arg2);
                ptr += 4;
            }
            case HALT -> {
            }
        }
    }

    public boolean halted() {
        return ptr < 0 || ptr >= mem.size() || mem.get(ptr) == OpCode.HALT.code;
    }

    public int memZero() {
        return mem.getFirst();
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
