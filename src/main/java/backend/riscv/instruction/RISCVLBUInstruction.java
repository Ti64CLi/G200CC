package backend.riscv.instruction;

import backend.riscv.operand.RISCVMemoryOperand;
import backend.riscv.RISCVOpcode;

public class RISCVLBUInstruction extends RISCVLoadInstruction {
    public RISCVLBUInstruction(RISCVMemoryOperand source) {
        super(RISCVOpcode.LBU, source);
    }
}
