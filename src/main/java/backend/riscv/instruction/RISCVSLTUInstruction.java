package backend.riscv.instruction;

import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVSLTUInstruction extends RISCVInstruction {
    public RISCVSLTUInstruction(RISCVRegisterOperand rs1, RISCVRegisterOperand rs2) {
        super(RISCVOpcode.SLTU, List.of(rs1, rs2));
        addResult();
    }
}
