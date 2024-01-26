package backend.riscv.instruction;

import backend.riscv.operand.RISCVImmediateOperand;
import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVSLTIInstruction extends RISCVInstruction {
    public RISCVSLTIInstruction(RISCVRegisterOperand rs1, RISCVImmediateOperand imm) {
        super(RISCVOpcode.SLTI, List.of(rs1, imm));
        addResult();
    }
}
