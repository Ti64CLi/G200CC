package backend;

import java.util.ArrayList;
import java.util.List;

public class BackendBlock implements BackendVisitableObject, LabelledBlock{

    private static long id = 0;

    private final String label;
    private final List<BackendInstruction> instructions;
    public List<BackendBlock> predecessors;
    public List<BackendBlock> successors;

    public BackendBlock() {
        label = "L" + id++;
        instructions = new ArrayList<>();
        predecessors = new ArrayList<BackendBlock>();
        successors = new ArrayList<BackendBlock>();
    }

    public void addInstruction(BackendInstruction instruction) {
        instruction.setContainingBlock(this);
        instructions.add(instruction);
    }
    
    public void addInstruction(BackendInstruction instruction, int index) {
        instruction.setContainingBlock(this);
        instructions.add(index, instruction);
    }
    

    public String getLabel() {
        return label;
    }

    public List<BackendInstruction> getInstructions() {
        return instructions;
    }

    public List<BackendBlock> getSuccessors() {
        return successors;
    }

    public List<BackendBlock> getPredecessors() {
        return predecessors;
    }

    @Override
    public Object accept(BackendVisitor v) {
        return v.visitBlock(this);
    }
    
    public String toString() {
    	String result = this.label + ":\n";
    	for (BackendInstruction i : instructions) {
    		result += "  " + i.toString() + "\n";
    	}
    	return result;
    }
}
