package backend.riscv.instruction;

import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVANDInstruction extends RISCVInstruction {
    public RISCVANDInstruction(RISCVRegisterOperand rs1, RISCVRegisterOperand rs2) {
        super(RISCVOpcode.AND, List.of(rs1, rs2));
        addResult();
    }
}
