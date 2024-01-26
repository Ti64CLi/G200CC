package backend.riscv.instruction;

import backend.riscv.*;
import backend.riscv.operand.RISCVImmediateOperand;

import java.util.List;

public class RISCVAUIPCInstruction extends RISCVInstruction {
    public RISCVAUIPCInstruction(RISCVImmediateOperand immediate) {
        super(RISCVOpcode.AUIPC, List.of(immediate));
        addResult();
    }
}
