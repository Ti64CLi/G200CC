package backend.riscv.instruction;

import java.util.List;

import backend.BackendBlock;
import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVOperand;

public abstract class RISCVBranchInstruction extends RISCVInstruction {
    private final List<BackendBlock> successors;
	
    protected RISCVBranchInstruction(RISCVOpcode opcode, List<RISCVOperand> operands, BackendBlock target) {
        super(opcode, operands);
    	successors = List.of(target);
    }

    public BackendBlock getTarget() {
        return successors.get(0);
    }
    
    public String toString() {
    	return super.toString() + ", " + successors.get(0).getLabel();
    }
}
