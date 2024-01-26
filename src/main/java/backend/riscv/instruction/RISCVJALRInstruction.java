package backend.riscv.instruction;

import backend.BackendBlock;
import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVJALRInstruction extends RISCVInstruction {

    public RISCVJALRInstruction(RISCVRegisterOperand rs1, boolean isGoto) {
        super(RISCVOpcode.JALR, List.of(rs1));
        
        if (isGoto)
        	addResult();
    }

}
