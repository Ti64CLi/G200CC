package backend.riscv.instruction;

import backend.riscv.operand.RISCVImmediateOperand;
import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVSLTIUInstruction extends RISCVInstruction {
    public RISCVSLTIUInstruction(RISCVRegisterOperand rs1, RISCVImmediateOperand imm) {
        super(RISCVOpcode.SLTIU, List.of(rs1, imm));
        addResult();
    }
}
