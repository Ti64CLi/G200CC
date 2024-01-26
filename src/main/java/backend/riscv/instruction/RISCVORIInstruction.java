package backend.riscv.instruction;

import backend.riscv.operand.RISCVImmediateOperand;
import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVORIInstruction extends RISCVInstruction {
    public RISCVORIInstruction(RISCVRegisterOperand rs1, RISCVImmediateOperand imm) {
        super(RISCVOpcode.ORI, List.of(rs1, imm));
        addResult();
    }
}
