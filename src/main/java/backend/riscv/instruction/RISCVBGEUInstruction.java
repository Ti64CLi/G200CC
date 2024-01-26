package backend.riscv.instruction;

import backend.BackendBlock;
import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVBGEUInstruction extends RISCVBranchInstruction {
    public RISCVBGEUInstruction(RISCVRegisterOperand rs1, RISCVRegisterOperand rs2, BackendBlock target) {
        super(RISCVOpcode.BGEU, List.of(rs1, rs2), target);
    }
}
