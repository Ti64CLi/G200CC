package backend.riscv.instruction;

import backend.BackendFunction;
import backend.riscv.RISCVOpcode;

import java.util.List;

public class RISCVCALLInstruction extends RISCVInstruction {
    private final BackendFunction function;

    public RISCVCALLInstruction(BackendFunction function) {
        super(RISCVOpcode.CALL, List.of());
        this.function = function;
        addResult();
    }

    public BackendFunction getFunction() {
        return function;
    }
}
