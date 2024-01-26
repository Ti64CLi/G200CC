package backend.riscv.instruction;

import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVMULHInstruction extends RISCVInstruction {
    public RISCVMULHInstruction(RISCVRegisterOperand rs1, RISCVRegisterOperand rs2) {
        super(RISCVOpcode.MULH, List.of(rs1, rs2));
        addResult();
    }
}
