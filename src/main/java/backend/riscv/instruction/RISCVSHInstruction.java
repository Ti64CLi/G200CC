package backend.riscv.instruction;

import backend.riscv.operand.RISCVMemoryOperand;
import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

public class RISCVSHInstruction extends RISCVStoreInstruction {
    public RISCVSHInstruction(RISCVRegisterOperand source, RISCVMemoryOperand destination) {
        super(RISCVOpcode.SH, source, destination);
    }
}
