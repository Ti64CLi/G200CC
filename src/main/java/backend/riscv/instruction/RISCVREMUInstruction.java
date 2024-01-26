package backend.riscv.instruction;

import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVREMUInstruction extends RISCVInstruction {
    public RISCVREMUInstruction(RISCVRegisterOperand rs1, RISCVRegisterOperand rs2) {
        super(RISCVOpcode.REMU, List.of(rs1, rs2));
        addResult();
    }
}
