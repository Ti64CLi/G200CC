package backend.riscv.instruction;

import backend.riscv.operand.RISCVMemoryOperand;
import backend.riscv.RISCVOpcode;

public class RISCVLWInstruction extends RISCVLoadInstruction {
    public RISCVLWInstruction(RISCVMemoryOperand source) {
        super(RISCVOpcode.LW, source);
    }
}
