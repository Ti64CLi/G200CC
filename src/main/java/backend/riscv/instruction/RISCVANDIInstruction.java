package backend.riscv.instruction;

import backend.riscv.operand.RISCVImmediateOperand;
import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVANDIInstruction extends RISCVInstruction {
    public RISCVANDIInstruction(RISCVRegisterOperand rs1, RISCVImmediateOperand imm) {
        super(RISCVOpcode.ANDI, List.of(rs1, imm));
        addResult();
    }
}
