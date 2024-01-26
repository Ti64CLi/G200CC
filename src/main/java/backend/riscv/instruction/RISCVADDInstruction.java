package backend.riscv.instruction;

import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVADDInstruction extends RISCVInstruction {
    public RISCVADDInstruction(RISCVRegisterOperand rs1, RISCVRegisterOperand rs2) {
        super(RISCVOpcode.ADD, List.of(rs1, rs2));
        addResult();
    }
}
