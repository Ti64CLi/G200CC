package backend.riscv.instruction;

import backend.riscv.*;
import backend.riscv.operand.RISCVImmediateOperand;

import java.util.List;

public class RISCVLUIInstruction extends RISCVInstruction {
    public RISCVLUIInstruction(RISCVImmediateOperand immediate) {
        super(RISCVOpcode.LUI, List.of(immediate));
        addResult();
    }
}
