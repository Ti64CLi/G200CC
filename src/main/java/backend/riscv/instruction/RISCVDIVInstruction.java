package backend.riscv.instruction;

import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVDIVInstruction extends RISCVInstruction {
    public RISCVDIVInstruction(RISCVRegisterOperand rs1, RISCVRegisterOperand rs2) {
        super(RISCVOpcode.DIV, List.of(rs1, rs2));
        addResult();
    }
}
