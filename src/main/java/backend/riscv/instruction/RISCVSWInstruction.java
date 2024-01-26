package backend.riscv.instruction;

import backend.riscv.operand.RISCVMemoryOperand;
import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

public class RISCVSWInstruction extends RISCVStoreInstruction {
    public RISCVSWInstruction(RISCVRegisterOperand source, RISCVMemoryOperand destination) {
        super(RISCVOpcode.SW, source, destination);
    }
}
