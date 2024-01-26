package backend.riscv.instruction;

import backend.riscv.operand.RISCVMemoryOperand;
import backend.riscv.RISCVOpcode;

public class RISCVLBInstruction extends RISCVLoadInstruction {
    public RISCVLBInstruction(RISCVMemoryOperand source) {
        super(RISCVOpcode.LB, source);
    }
}
