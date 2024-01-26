package backend.riscv.instruction;

import backend.riscv.operand.RISCVMemoryOperand;
import backend.riscv.RISCVOpcode;

public class RISCVLHInstruction extends RISCVLoadInstruction {
    public RISCVLHInstruction(RISCVMemoryOperand source) {
        super(RISCVOpcode.LH, source);
    }
}
