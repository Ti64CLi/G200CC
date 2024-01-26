package backend.riscv.instruction;

import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVSRAInstruction extends RISCVInstruction {
    public RISCVSRAInstruction(RISCVRegisterOperand rs1, RISCVRegisterOperand rs2) {
        super(RISCVOpcode.SRA, List.of(rs1, rs2));
        addResult();
    }
}
