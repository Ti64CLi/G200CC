package backend.riscv.instruction;

import backend.BackendBlock;
import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVBLTUInstruction extends RISCVBranchInstruction {
    public RISCVBLTUInstruction(RISCVRegisterOperand rs1, RISCVRegisterOperand rs2, BackendBlock target) {
        super(RISCVOpcode.BLTU, List.of(rs1, rs2), target);
    }
}
