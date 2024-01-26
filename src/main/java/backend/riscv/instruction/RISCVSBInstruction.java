package backend.riscv.instruction;

import backend.riscv.operand.RISCVMemoryOperand;
import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

public class RISCVSBInstruction extends RISCVStoreInstruction {
    public RISCVSBInstruction(RISCVRegisterOperand source, RISCVMemoryOperand destination) {
        super(RISCVOpcode.SB, source, destination);
    }
}
