package backend.riscv.instruction;

import backend.riscv.operand.RISCVMemoryOperand;
import backend.riscv.RISCVOpcode;

import java.util.List;

public abstract class RISCVLoadInstruction extends RISCVInstruction {
    protected RISCVLoadInstruction(RISCVOpcode opcode, RISCVMemoryOperand source) {
        super(opcode, List.of(source));
        addResult();
    }
}
