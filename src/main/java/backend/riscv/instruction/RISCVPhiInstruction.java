package backend.riscv.instruction;

import backend.riscv.operand.RISCVRegisterOperand;

import java.util.List;

public class RISCVPhiInstruction extends RISCVInstruction {
    public RISCVPhiInstruction(List<RISCVRegisterOperand> operands) {
        super(null, operands);
        addResult();
    }
    
    public String toString() {
    	return this.getResult() + " = phi " + this.getOperands().toString();
    }
    
}
