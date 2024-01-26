package backend.riscv.instruction;

import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVMULHUInstruction extends RISCVInstruction {
    public RISCVMULHUInstruction(RISCVRegisterOperand rs1, RISCVRegisterOperand rs2) {
        super(RISCVOpcode.MULHU, List.of(rs1, rs2));
        addResult();
    }
}
