package backend.riscv.instruction;

import backend.riscv.operand.RISCVMemoryOperand;
import backend.riscv.RISCVOpcode;

public class RISCVLHUInstruction extends RISCVLoadInstruction {
    public RISCVLHUInstruction(RISCVMemoryOperand source) {
        super(RISCVOpcode.LHU, source);
    }
}
