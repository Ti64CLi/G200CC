package backend.riscv.instruction;

import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVSLLInstruction extends RISCVInstruction {
    public RISCVSLLInstruction(RISCVRegisterOperand rs1, RISCVRegisterOperand rs2) {
        super(RISCVOpcode.SLL, List.of(rs1, rs2));
        addResult();
    }
}
