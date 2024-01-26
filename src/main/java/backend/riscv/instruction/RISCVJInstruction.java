package backend.riscv.instruction;

import backend.BackendBlock;
import backend.riscv.RISCVOpcode;

import java.util.List;

public class RISCVJInstruction extends RISCVInstruction {
	
    private final List<BackendBlock> successors;
	
    public RISCVJInstruction(BackendBlock target) {
        super(RISCVOpcode.J, List.of());
        successors = List.of(target);
    }
    
    public BackendBlock getTarget() {
    	return successors.get(0);
    }
}
