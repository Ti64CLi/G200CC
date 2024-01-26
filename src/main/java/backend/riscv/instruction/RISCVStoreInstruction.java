package backend.riscv.instruction;

import backend.riscv.operand.RISCVMemoryOperand;
import backend.riscv.RISCVOpcode;
import backend.riscv.RISCVRegister;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public abstract class RISCVStoreInstruction extends RISCVInstruction {
    protected RISCVStoreInstruction(RISCVOpcode opcode, RISCVRegisterOperand source, RISCVMemoryOperand destination) {
        super(opcode, List.of(source, destination));
    }

    @Override
    public RISCVRegister getResult() {
        return null;
    }
}
